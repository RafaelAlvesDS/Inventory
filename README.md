# Inventory App

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Inventory App Logo" width="100"/>
</p>

Um aplicativo Android de gerenciamento de inventário desenvolvido como parte do curso **Android Basics Nanodegree** da **Udacity**. Este projeto demonstra o uso de SQLite, Content Providers, CursorLoaders e outras tecnologias fundamentais do Android.

## 📱 Funcionalidades

- ✅ **Visualizar Inventário**: Lista todos os itens cadastrados com nome, preço e quantidade
- ✅ **Adicionar Itens**: Interface para cadastrar novos produtos no inventário
- ✅ **Editar Itens**: Modificar informações de produtos existentes
- ✅ **Excluir Itens**: Remover produtos individuais ou todos os itens
- ✅ **Validação de Dados**: Verificação de campos obrigatórios antes de salvar
- ✅ **Armazenamento Local**: Dados persistidos em banco SQLite
- ✅ **Interface Intuitiva**: Design seguindo Material Design Guidelines

## 🏗️ Arquitetura

O aplicativo segue o padrão de arquitetura recomendado pelo Android, utilizando:

- **SQLite Database**: Armazenamento local dos dados
- **Content Provider**: Abstração para acesso aos dados
- **CursorLoader**: Carregamento assíncrono de dados
- **CursorAdapter**: Adaptador para exibição dos dados em ListView

### Estrutura de Dados

O banco de dados possui uma tabela `items` com os seguintes campos:

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `_id` | INTEGER | Chave primária |
| `name` | TEXT | Nome do produto |
| `price` | REAL | Preço do produto |
| `quantity` | INTEGER | Quantidade em estoque |
| `supplier_email` | TEXT | Email do fornecedor |
| `image` | TEXT | Caminho da imagem (futuro) |

## 🛠️ Tecnologias Utilizadas

- **Java**: Linguagem principal
- **Android SDK 26**: Versão do Android utilizada
- **SQLite**: Banco de dados local
- **Content Provider**: Gerenciamento de dados
- **Material Design**: Interface do usuário
- **FloatingActionButton**: Botão de ação principal

## 📦 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/android/rafael/inventory/
│   │   ├── InventoryActivity.java      # Tela principal com lista de itens
│   │   ├── EditorActivity.java         # Tela de adição/edição de itens
│   │   ├── ItemCursorAdapter.java      # Adaptador para ListView
│   │   └── data/
│   │       ├── ItemContract.java       # Contrato do banco de dados
│   │       ├── ItemDbHelper.java       # Helper do SQLite
│   │       └── ItemProvider.java       # Content Provider
│   ├── res/
│   │   ├── layout/                     # Layouts XML
│   │   ├── menu/                       # Menus da aplicação
│   │   ├── values/                     # Strings, cores, dimensões
│   │   └── mipmap-*/                   # Ícones da aplicação
│   └── AndroidManifest.xml
└── build.gradle
```

## 🚀 Como Executar

### Pré-requisitos

- Android Studio 3.0 ou superior
- Android SDK 19 (KitKat) ou superior
- Dispositivo Android ou Emulador

### Passos para Execução

1. **Clone o repositório**
   ```bash
   git clone https://github.com/RafaelAlvesDS/Inventory.git
   ```

2. **Abra no Android Studio**
   - Abra o Android Studio
   - Selecione "Open an existing Android Studio project"
   - Navegue até a pasta do projeto e selecione

3. **Execute o aplicativo**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" (▶️) ou use `Shift + F10`

## 📱 Capturas de Tela

### Tela Principal (Lista de Itens)
- Lista todos os produtos cadastrados
- Floating Action Button para adicionar novos itens
- Menu com opção para deletar todos os itens

### Tela de Edição
- Formulário para cadastrar/editar produtos
- Validação de campos obrigatórios
- Opções para salvar ou deletar

## 🎯 Objetivos de Aprendizado

Este projeto foi desenvolvido para demonstrar competências em:

- [x] Uso de SQLite para armazenamento local
- [x] Implementação de Content Providers
- [x] Utilização de CursorLoaders para carregamento assíncrono
- [x] Criação de interfaces com Material Design
- [x] Validação de entrada de dados
- [x] Navegação entre Activities
- [x] Gerenciamento de menus e ações


## 👨‍💻 Desenvolvedor

**Rafael Alves**
- GitHub: [@RafaelAlvesDS](https://github.com/RafaelAlvesDS)

## 📚 Curso

Este projeto foi desenvolvido como parte do curso:
- **Android Basics Nanodegree** - Udacity
- **Data Storage** - Seção sobre armazenamento de dados

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte do curso da Udacity.

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**
