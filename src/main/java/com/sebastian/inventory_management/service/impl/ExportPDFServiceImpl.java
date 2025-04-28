package com.sebastian.inventory_management.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.OrderItem.OrderItemResponseDTO;
import com.sebastian.inventory_management.service.IExportPDFService;

@Service
public class ExportPDFServiceImpl implements IExportPDFService {

    @Override
    public ByteArrayInputStream exportOrderDetailsToPDF(OrderResponseDTO order) throws IOException {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Detalles de la Orden #" + order.getOrderNumber(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Fecha de Orden: " + order.getOrderDate().toString(), infoFont));
            document.add(new Paragraph("Proveedor: " + order.getSupplierName() + " (ID: " + order.getSupplierId() + ")",
                    infoFont));
            document.add(Paragraph.getInstance("\n"));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 4, 2, 3, 3 });

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Producto", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Cantidad", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Precio Unit.", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Subtotal", headFont));
            table.addCell(hcell);

            for (OrderItemResponseDTO item : order.getItems()) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(item.getProductName()));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity())));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.format("$ %.2f", item.getPrice())));
                table.addCell(cell);

                BigDecimal price = item.getPrice();
                int quantity = item.getQuantity();

                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
                cell = new PdfPCell(new Phrase(String.format("$ %.2f", subtotal)));
                table.addCell(cell);
            }

            document.add(table);

            document.add(Chunk.NEWLINE);

            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph total = new Paragraph(
                    "Total de Orden: $" + String.format("%.2f", calculateOrderTotal(order.getItems())), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (DocumentException ex) {
            throw new IOException(ex.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private Float calculateOrderTotal(List<OrderItemResponseDTO> items) {
        return (float) items.stream()
                .mapToDouble(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue())
                .sum();
    }

}
