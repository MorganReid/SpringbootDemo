# Gateway

repo for gateway api.
The design doc can be found [here](https://docs.google.com/document/d/1V2ode8b8CSfoZdzFjKWaRA9fAOntBsnsldmrVY-HLt0/edit).

# How to build and deploy SecGateway
## Prerequisites
* Ubuntu
* JDK 17.0.3 or higher

All secrets in this project are safely stored in the Vault. Before compiling, running or testing the project,
please retrieve the secrets by running the following
   ```
   bash scripts/secret_manager.sh
   ```
This script can also be used to remove secrets.






