# SCC Empirical Study

This repo contains the source code and dataset for SCC empirical study.

- Source Code is in directory src/cn/edu/njust/scc
- Data set is in folder data.

## How to run SCC program

- After extracting dependency relations for modified files in a commit (A reverse engineering tool Understand is used in this process), we detected SCCs before and after the commit. Then we compared the SCCs before and after the commit to identify SCCs' evolution types. Last we extracted evolved SCC instances out from  all modified files (The program is in directory src/cn/edu/njust/scc/datacollect and the data is data/evolution-status).
- With evolved SCC instances, we can do the analysis in this study, such as calculating the size and diamter, identifying the shape and shape transtion of SCCs, checking dependecy changes along with SCC's evolution, etc. (The program is in directory src/cn/edu/njust/scc/analysis)
- We also added some discussion analysis code in the directory src/cn/edu/njust/scc/eval.
