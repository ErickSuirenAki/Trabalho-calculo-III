# 📐 Calculadora e Visualizador 3D de Peças CAD (Cálculo III)

## 🏫 Informações Institucionais

* **Instituição:** Fundação Universidade Federal de Rondônia
* **Curso:** Ciência da Computação
* **Disciplina:** Cálculo III
* **Professor(a):** Prof. Esp. Rodrigo Duarte de Oliveira Tolêdo 
* **Período:** 2º Semestre / 2026

### 👥 Integrantes do Grupo

* Anna Júlia Oliveira de Sousa 
* Bruna Caculakis Santos 
* Camila Fernanda de Araujo Silva 
* Erick Batista dos Santos 
* Lara Pereira Ferreira 
* Maurício de Oliveira Pinto Júnior

---

## 🔍 Visão Geral do Projeto

Este projeto consiste em um aplicativo Android desenvolvido em **Java e OpenGL ES 3.0** projetado como uma ferramenta interativa para **Engenharia e Física Computacional**. O aplicativo permite a importação de arquivos de malhas tridimensionais (no formato **STL Binário ou ASCII**) e a definição da densidade do material.

A partir dos dados geométricos e físicos, a aplicação utiliza **Integração Numérica e Cálculo Vetorial** para processar e exibir em tempo real:

* Volume do objeto 3D;
* Massa total;
* Centro de massa $(\bar{x}, \bar{y}, \bar{z})$;
* Renderização 3D interativa do modelo com marcador do centro de massa e normalização de escala/orientação.

---

## 📐 Conceitos de Cálculo Aplicados

### 1. Decomposição Geometrica em Tetraedros

A malha do objeto STL é composta por uma coleção de triângulos na superfície. Para aplicar o **Teorema da Divergência (Gauss)** em uma malha arbitrária, cada triângulo de vértices $\mathbf{r}_1, \mathbf{r}_2, \mathbf{r}_3$ forma um tetraedro orientado com a origem $(0,0,0)$.

### 2. Cálculo do Volume via Produto Misto

O volume orientado (com sinal) de cada tetraedro é calculado pelo produto misto dos vetores dos vértices dividido por 6:

$$V_i = \frac{1}{6} \left( \mathbf{r}_1 \cdot (\mathbf{r}_2 \times \mathbf{r}_3) \right)$$

O volume total do corpo rígido é a soma absoluta dos volumes orientados de todos os tetraedros:

$$V = \left\vert{} \sum_{i=1}^{N} V_i \right\vert{}$$

### 3. Centro de Massa via Média Ponderada

O centróide $\mathbf{c}_i$ de cada tetraedro (com um dos vértices na origem) é a média aritmética simples dos seus vértices:

$$\mathbf{c}_i = \frac{\mathbf{r}_1 + \mathbf{r}_2 + \mathbf{r}_3}{4}$$

Para densidade uniforme ($\rho$), o Centro de Massa Global $(\bar{x}, \bar{y}, \bar{z})$ é dado pela soma dos centróides ponderada pelos volumes orientados de cada tetraedro sobre o volume total:

$$\bar{x} = \frac{\sum (c_{i, x} \cdot V_i)}{\sum V_i}, \quad \bar{y} = \frac{\sum (c_{i, y} \cdot V_i)}{\sum V_i}, \quad \bar{z} = \frac{\sum (c_{i, z} \cdot V_i)}{\sum V_i}$$

---

## 🛠️ Tecnologias e Dependências

* **Linguagem Principal:** Java (Compatível com Android SDK 21+)


* **Interface Gráfica e UI:** Android Jetpack / AppCompat


* **Renderização Gráfica 3D:** OpenGL ES 3.0 (`GLSurfaceView` e Shaders em GLSL)


* **Manipulação e Conversão de Dados:** Java NIO (`ByteBuffer`, `ByteOrder`)



---

## 📂 Estrutura do Repositório

```text
app/src/main/java/com/example/trabalhocalculo3/
├── MainActivity.java           # Orquestração do ciclo de vida da UI e eventos
├── graphics/                   # Camada de renderização OpenGL ES
│   ├── GraphRenderer.java      # Pipeline do OpenGL, shaders e transformações MVP
│   ├── GraphSurfaceView.java   # View Android customizada para exibição 3D
│   └── Sphere.java             # Gerador geométrico para o indicador do Centro de Massa
├── mathMotor/                  # Módulo de cálculo numérico isolado da UI
│   ├── VolumeCalculator.java   # Algoritmo de cálculo de volume orientado
│   ├── MassCalculator.java     # Algoritmo de cálculo do Centro de Massa
│   └── ConvertUnits.java       # Utilitário para conversão de unidades (mm, cm, m)
├── mesh/                       # Estruturas e importação de modelos 3D
│   ├── Mesh3D.java             # POJO da malha (vértices, normais, bounding box)
│   ├── MeshImporter.java       # Parser genérico para STL (Binário e ASCII)
│   └── StlFormat.java          # Enum para identificação de formato (BINARY, ASCII, INVALID)
└── util/
    └── Unit.java               # Definições de unidades de medida

```

---

## 🚀 Como Executar o Projeto

### 1. Clonar o Repositório

```bash
git clone https://github.com/ErickSuirenAki/Trabalho-calculo-III
cd Trabalho-calculo-III

```

### 2. Abrir no Android Studio

1. Abra o **Android Studio**.
2. Selecione **Open an Existing Project**.
3. Navegue até a pasta clonada e selecione-a.
4. Aguarde a sincronização do Gradle.

### 3. Executar o Aplicativo

* Conecte um dispositivo físico Android via depuração USB ou inicie um Emulador Android (API 21 ou superior).
* Pressione **Run** (`Shift + F10`).

---

## 📈 Exemplos de Uso e Resultados

1. **Importação:** Clique no botão de importação e selecione qualquer arquivo `.stl` (binário ou ASCII) via *Storage Access Framework*.


2. **Seleção de Unidades:** Escolha a unidade de medida do arquivo original (mm, cm ou m).


3. **Ajuste de Densidade:** Mova o slider de densidade para recalcular a massa em tempo real.


4. **Visualização 3D:** O objeto é renderizado automaticamente com normalização de escala e reorientação de coordenadas (Z-Up para Y-Up), contendo um marcador visual vermelho indicando a posição exata do **Centro de Massa**.



---

## 💡 Conclusão e Desafios Superados

* **Compatibilidade de Arquivos:** Ajustes de leitura de bytes com `readAllBytes` retrocompatível para evitar falhas em versões legadas do Android.


* **Sistemas de Coordenadas:** Conversão do padrão CAD/STL (onde a vertical é o eixo $Z$) para o padrão OpenGL ES (onde a vertical é o eixo $Y$) através de matrizes de rotação no eixo $X$.


* **Normalização Automática de Escala:** Implementação da *Bounding Box* para centralizar objetos com origens deslocadas e enquadrar dinamicamente peças de qualquer dimensão no campo de visão da câmera.
