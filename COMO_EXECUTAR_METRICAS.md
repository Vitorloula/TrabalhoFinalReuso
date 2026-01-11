# 📊 Como Executar as Ferramentas de Métricas

Este guia explica como rodar as ferramentas de análise de código para obter as métricas de reusabilidade do projeto.

---

## 📋 Pré-requisitos

- **Java 11+** instalado e configurado no PATH
- **Maven 3.6+** instalado
- Terminal/PowerShell

---

## 1. CK Metrics (CBO, LCOM, WMC, RFC)

O CK Metrics analisa métricas de Chidamber-Kemerer para classes Java.

### Executar:

```powershell
# Na pasta raiz do projeto
java -jar ck-0.7.0-jar-with-dependencies.jar "src/main/java" false 0 false ./
```

### Parâmetros:

| Parâmetro | Valor           | Descrição                     |
| --------- | --------------- | ----------------------------- |
| 1º        | `src/main/java` | Diretório do código fonte     |
| 2º        | `false`         | Usar jars? (não)              |
| 3º        | `0`             | Máximo de arquivos por thread |
| 4º        | `false`         | Análise de variáveis?         |
| 5º        | `./`            | Diretório de saída            |

### Arquivos Gerados:

- `.class.csv` - Métricas por classe
- `.method.csv` - Métricas por método

### Calcular Médias (PowerShell):

```powershell
$csv = Import-Csv '.class.csv'
$avgCBO = ($csv | Measure-Object -Property cbo -Average).Average
$avgLCOM = ($csv | Where-Object { $_.lcom -ne 'NaN' } | Measure-Object -Property lcom -Average).Average
$avgWMC = ($csv | Measure-Object -Property wmc -Average).Average
$avgRFC = ($csv | Measure-Object -Property rfc -Average).Average

Write-Host "CBO Médio: $avgCBO"
Write-Host "LCOM Médio: $avgLCOM"
Write-Host "WMC Médio: $avgWMC"
Write-Host "RFC Médio: $avgRFC"
Write-Host "Total Classes: $($csv.Count)"
```

### Interpretação das Métricas:

| Métrica  | Significado                               | Ideal |
| -------- | ----------------------------------------- | ----- |
| **CBO**  | Coupling Between Objects (acoplamento)    | < 5   |
| **LCOM** | Lack of Cohesion of Methods (coesão)      | < 3   |
| **WMC**  | Weighted Methods per Class (complexidade) | < 10  |
| **RFC**  | Response For a Class (métodos chamados)   | < 50  |
| **DIT**  | Depth of Inheritance Tree                 | < 4   |

---

## 2. PMD (Violações de Código)

O PMD detecta problemas de qualidade de código.

### Executar:

```powershell
mvn pmd:pmd
```

### Relatório Gerado:

- `target/pmd.xml`
- `target/site/pmd.html` (visualizar no navegador)

---

## 3. CPD (Código Duplicado)

O CPD (Copy-Paste Detector) encontra código duplicado.

### Executar:

```powershell
mvn pmd:cpd
```

### Relatório Gerado:

- `target/cpd.xml`
- `target/site/cpd.html`

---

## 4. Compilar e Verificar Erros

Antes de rodar as métricas, certifique-se que o projeto compila:

```powershell
mvn clean compile
```

---

## 5. Resumo Rápido

```powershell
# 1. Compilar projeto
mvn clean compile

# 2. Rodar CK Metrics
java -jar ck-0.7.0-jar-with-dependencies.jar "src/main/java" false 0 false ./

# 3. Rodar PMD
mvn pmd:pmd

# 4. Rodar CPD
mvn pmd:cpd

# 5. Ver resultados
# - CK Metrics: .class.csv e .method.csv
# - PMD: target/pmd.xml
# - CPD: target/cpd.xml
```

---

## 📁 Estrutura de Arquivos Gerados

```
inventory-management-API/
├── .class.csv          ← Métricas CK por classe
├── .method.csv         ← Métricas CK por método
└── target/
    ├── pmd.xml         ← Violações PMD
    ├── cpd.xml         ← Duplicações CPD
    └── site/
        ├── pmd.html    ← Relatório visual PMD
        └── cpd.html    ← Relatório visual CPD
```

---

## 🔧 Troubleshooting

### Erro: "java não é reconhecido"

→ Instale o JDK e adicione ao PATH do sistema

### Erro: "mvn não é reconhecido"

→ Instale o Maven e adicione ao PATH do sistema

### Erro no CK Metrics

→ Verifique se o arquivo JAR `ck-0.7.0-jar-with-dependencies.jar` está na pasta raiz

---

**Última atualização:** 2026-01-11
