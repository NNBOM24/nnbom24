# 🔍 Demystifying the Evolution of Neural Networks with BOM Analysis: Insights from a Large-Scale Studyof 55,997 GitHub Repositories

Welcome to the official repository of our dataset and tools for analyzing the evolution of neural network components across a large-scale collection of GitHub repositories.

---

## 📁 Repository Structure

```text
nnbom24/
├── component_analysis/               # Source code for component analysis
├── data_source/                      # Metadata and classifications of analyzed repositories
├── database_info/                    # CSV files with dataset-related information
├── data_collection_script/           # Scripts to crawl repositories and their versions
├── AI_component_analysis-1.0-SNAPSHOT.jar  # AI component analysis tool (executable)

```

##  🧪 Component Analysis Tool

### Requirements

- **JDK** 16 or higher
- **ANTLR4** (Install from: https://www.antlr.org/)

### Installation & Usage

```bash
# Clone this repository
git clone https://github.com/NNBOM24/nnbom24.git

# Navigate into the project directory
cd nnbom24

# Run the AI component analysis tool
java -jar AI_component_analysis-1.0-SNAPSHOT.jar -input [source_dir] -output [output_dir] -thread [num_threads] -mt [num_tokens]
```

### Options
| Parameter    | Description                                             | Default           |
| --------- | ------------------------------------------------------- | ----------------- |
| `-input`  | Input directory containing a collection of repositories | *Required*        |
| `-output` | Output directory to store result files                  | Current directory |
| `-thread` | Number of threads used for parallel execution           | 1                 |
| `-mt` |The minimum number of tokens for a neural network module           | 50                 |




### Output Files

- **`projectInfo.csv`**
    
    Records the third-party libraries and pre-trained models used in each project.

    Format: `[projectID, projectName, imports, pretrainedModels]`

- **`moduleInfo.csv`**
    
    Records detailed information about detected neural network modules.

    Format: `[moduleID, moduleProjectID, modulePath, moduleName, startLine, endLine, moduleHash]`

- **`projectID_report.txt`**

    Per-repository report file, including:

    - Usage statistics of dependencies (frequency, first used, etc.)
    - Module traceability analysis
    - Most similar repositories



## 🏗️ Building Your Own BOM Database
If you have a large set of AI projects and wish to build your own BOM (Bill of Materials) database:
```bash
# Navigate to the Java source directory
cd component_analysis/src/main/java

# Compile and run the analysis program
javac Main.java && java Main
```

### Output Files

- **`moduleInfo.csv`**
    
    `[moduleID, moduleProjectID, modulePath, moduleName, startLine, endLine, moduleHash]`

- **`modulesClone.csv`**
    
    Lists clone families of modules.
    Rows belonging to the same clone family are grouped together, with different families separated by blank lines.

- **`projectInfo.csv`**

    `[projectID, projectName, imports, pretrainedModels]`

## 📂 Directory Details

**`component_analysis/`**
    
Contains the core source code for component and module analysis.

**`data_source/`**
    
Records the sources and classification of the analyzed GitHub repositories:

- github_repositories_data.csv: Metadata of each repository

- version_download_link: Download URLs for all repository versions

- repo_classification: Classification of all repositories into 8 domains

**`database_info/`**
Contains datasets supporting further analysis:

- original_modules_info.csv: All original PyTorch-based neural network modules from GitHub

- dependencies.csv: Repository-level dependency relationships

    - dependency_repo_id: The depended-on repository

    - dependent_repo_id: The dependent repository

## 📫 Contact
If you have any questions, feedback, or would like to contribute, feel free to open an issue or reach out to the project maintainers.

