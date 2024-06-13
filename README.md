# Demystifying the Evolution of Neural Networks with BOM Analysis in 68,200 GitHub Repositories

## Introduction
Welcome to the repository for our dataset and associated tools. This repository is organized into three main directories: `component_analysis`, `data_source`, and `database_info`. Each directory contains specific files related to our project, which are detailed below.

## Directory Structure
The repository is structured as follows:

- `component_analysis/`: Contains the source code for component analysis.
- `data_source/`: Records the sources of the repositories.
- `database_info/`: Records the database infomatoin.

## Directory Details

### component_analysis/
This directory includes the tools used for analyzing the components of the neural network datasets.

#### Requirements
- JDK 8+
- Antlr4(Installation address:`https://www.antlr.org/`)

#### Install & Usage
- Move into /src/main/java directory (`cd /src/main/java`) 
- Run Project (`javac Main.java && java Main`)
- Tool configuration can be set in `global/Data.java`.

#### Output file format:
- moduleInfo.csv
`[moduleID,moduleProjectID,modulePath,moduleName,startLine,endLine,moduleHash]` Record detailed information about the neural network modules.
- modulesClone.csv
`[moduleID,moduleProjectID,modulePath,moduleName,startLine,endLine,moduleHash]` Adjacent rows belong to the same clone family, with different clone families separated by blank lines.
- projectInfo.csv
`[projectID,projectName,imports,pretrainedModels]` Record the third-party libraries that the project depends on and the pre-trained models it invokes.


### data_source/
This directory documents the sources of the repositories used in our dataset.

- `github_repositories_data.csv`: Record the metadata of the GitHub repositories we analyzed.
- `version_download_link`: Record the download links for all repository versions.
- `repo_classification`: Recorded information on all repositories belonging to 8 different domains.
### database_info/
This directory contains CSV files, each recording different pieces of information related to the datasets.

- `original_modules_info.csv`: Recorded all original neural network modules based on the PyTorch architecture available on the GitHub platform.
- `dependencies.csv`: Recorded the dependency relationships between repositories. The `dependency_repo_id`: records the repositories that a repository depends on, while the `dependent_repo_id` records the repositories that depend on it.

## Usage
To use the tools and dataset in this repository, you can clone the repository and navigate to the respective directories to find the relevant files.

