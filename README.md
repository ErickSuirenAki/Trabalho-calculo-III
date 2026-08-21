```# 📊 Otimização de Infraestrutura Cloud via Derivadas Numéricas

## 🏫 Informações Institucionais
* **Instituição:** [Nome da Universidade]
* **Curso:** Ciência da Computação
* **Disciplina:** Cálculo Diferencial e Integral [I, II ou Numérico]
* **Professor(a):** Prof. Dr(a). [Nome do Professor]
* **Período:** [Ex: 2º Semestre / 2026]

### 👥 Integrantes do Grupo
* [Nome do Aluno 1] - RA: [00000000]
* [Nome do Aluno 2] - RA: [00000000]
* [Nome do Aluno 3] - RA: [00000000]

---

## 🔍 Visão Geral do Projeto
Este projeto acadêmico aplica conceitos de **Cálculo Diferencial** para resolver um problema real de Engenharia de Software: a **otimização de custos e alocação de servidores em nuvem**. 

O software simula o comportamento de tráfego de um sistema web e encontra matematicamente o ponto mínimo da curva de custo operacional, garantindo a máxima eficiência com o menor gasto financeiro possível.

---

## 📐 Conceitos de Cálculo Aplicados

### 1. Modelagem da Função Custo
A taxa de gasto por hora de uma infraestrutura que processa \(x\) requisições simultâneas é modelada pela função:

\[C(x) = ax^2 + \frac{b}{x} + c\]

Onde:
* \(ax^2\) representa o custo de processamento e energia (cresce exponencialmente com a carga).
* \(\frac{b}{x}\) representa o custo de ociosidade do servidor (quanto menos requisições por máquina, maior o custo fixo desperdiçado por requisição).
* \(c\) representa os custos contratuais fixos da nuvem.

### 2. Otimização via Derivadas (Ponto de Mínimo)
Para encontrar o número ideal de requisições por servidor que minimiza o custo, o algoritmo busca o ponto crítico onde a **primeira derivada da função é igual a zero**:

\[C'(x) = 2ax - \frac{b}{x^2} = 0\]

### 3. Abordagem Computacional (Cálculo Numérico)
Como em cenários reais as funções de custo podem ser complexas e não-lineares, o sistema utiliza o **Método de Newton-Raphson** para encontrar a raiz da derivada (\(C'(x) = 0\)) de forma iterativa:

\[x_{n+1} = x_n - \frac{C'(x_n)}{C''(x_n)}\]

---

## 🛠️ Tecnologias e Dependências

O projeto foi desenvolvido utilizando as seguintes ferramentas:

* **Linguagem Principal:** Python 3.10+
* **Interface Gráfica e Plots:** Matplotlib (versão 3.7+)
* **Processamento Vetorial:** NumPy (versão 1.24+)

---

## 📂 Estrutura do Repositório

```text
├── src/                  # Código-fonte do projeto
│   ├── main.py           # Ponto de entrada do programa e interface
│   ├── calculus.py       # Algoritmos matemáticos (Newton-Raphson, derivadas)
│   └── config.py         # Constantes e modelagem das funções de custo
├── docs/                 # Relatórios acadêmicos e PDFs (se houver)
├── README.md             # Documentação principal
└── requirements.txt      # Arquivo de dependências do Python
```

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para rodar a aplicação na sua máquina local:

### 1. Clonar o Repositório
```bash
git clone https://github.com[seu-usuario]/[nome-do-repositorio].git
cd [nome-do-repositorio]
```

### 2. Instalar as Dependências
Recomenda-se o uso de um ambiente virtual (venv):
```bash
python -m venv venv
source venv/bin/activate  # No Windows use: venv\Scripts\activate
pip install -r requirements.txt
```

### 3. Rodar a Aplicação
```bash
python src/main.py
```

---

## 📈 Exemplos de Uso e Resultados

Ao executar o script, o sistema processa os coeficientes configurados e gera um gráfico interativo mostrando a convergência para o ponto mínimo.

### Cenário de Teste Padrão
* **Entrada:** Coeficientes $a = 0.005$, $b = 2000$, $c = 50$
* **Chute Inicial ($x_0$):** 50 requisições
* **Resultado:** O algoritmo convergiu em **5 iterações**.
* **Ponto Ótimo Calculado:** $x \approx 58.48$ requisições por servidor.
* **Custo Mínimo:** $R\$ 101,24$ por hora.

*(Insira aqui um print do gráfico gerado pelo seu programa)*
![Gráfico de Otimização de Custos](https://placeholder.com)

---

## 💡 Conclusão e Aprendizados

O desenvolvimento deste projeto permitiu consolidar a relação teórica entre o Cálculo Diferencial e a otimização de algoritmos. No contexto da Ciência da Computação, compreender que problemas contínuos (Cálculo) precisam ser aproximados por métodos discretos (Computação Numérica) é fundamental para construir sistemas de alta performance. 

**Limitação encontrada:** O Método de Newton-Raphson mostrou-se extremamente rápido, porém altamente sensível ao "chute inicial" ($x_0$). Caso o valor inicial seja muito distante ou próximo de um ponto onde $C''(x) = 0$, o algoritmo diverge, evidenciando a necessidade de validações de intervalo (como o Teorema de Bolzano) em sistemas comerciais.
```

O arquivo deve ter aproximadamente esta estrutura
