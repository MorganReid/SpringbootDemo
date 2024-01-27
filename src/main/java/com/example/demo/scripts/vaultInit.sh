#!/bin/bash

# Vault server address
VAULT_ADDR=https://dev.vault.autox.tech:50036
VAULT_CLIENT_CRT_PATH=/usr/share/client-certs/AutoX_Client.crt
VAULT_CLIENT_KEY_PATH=/usr/share/client-certs/AutoX_Client.key
VAULT_JSON_FILE="$(mktemp /tmp/vault.XXXXX.json)"

readonly TXT_BOLD="\e[1m" && TXT_RED="\e[31m" && TXT_GREEN="\e[32m" && TXT_WHITE="\e[34m" && TXT_CLEAR="\e[0m"
unseal_tokens=(
  "ZF8VTlNE9pBIBLIf7wBol9QQVewKEfLfbEzzKDMpx18i"
  "n5fcL+heMQOLVyeSk11v7vm7GHtjr07JdmHdod7MjlET"
  "vu+G9aMtjXoi226a93wmnAtcnnylyvaIUJR3f5pJbsia"
)

function error() {
  (echo >&2 -e "[${TXT_RED}[ ERROR ]${TXT_CLEAR}] $*")
}

function ok() {
  (echo >&2 -e "[${TXT_GREEN}${TXT_BOLD}[ OK ]${TXT_CLEAR}] $*")
}

function info() {
  (echo >&2 -e "[${TXT_WHITE}${TXT_BOLD}[ INFO ]${TXT_CLEAR}] $*")
}

INITIALIZED="$(
  curl \
    -k \
    --silent \
    --key "$VAULT_CLIENT_KEY_PATH" \
    --cert "$VAULT_CLIENT_CRT_PATH" \
    "$VAULT_ADDR/v1/sys/init" | jq ".initialized"
)"

SEALED="$(
  curl \
    -k \
    --silent \
    --key /usr/share/client-certs/AutoX_Client.key \
    --cert /usr/share/client-certs/AutoX_Client.crt \
    "$VAULT_ADDR/v1/sys/seal-status" | jq ".sealed"
)"

if [[ "$INITIALIZED" = "false" ]]; then
  info "Initializing Vault..."
  # 初始化并将获取的解封密钥保存
  curl "$VAULT_ADDR/v1/sys/init" \
    -k \
    --request POST \
    --silent \
    --key /usr/share/client-certs/AutoX_Client.key \
    --cert /usr/share/client-certs/AutoX_Client.crt \
    --header 'Content-Type: application/json' \
    --data '{"secret_shares": 5, "secret_threshold": 3}' \
    --output "$VAULT_JSON_FILE"
  if [[ ! -f "$VAULT_JSON_FILE" ]]; then
    error "Initializing Vault Error"
    exit 1
  else
    ok "Initializing Vault completed"
  fi

else
  info "Vault is already initialized."
fi

# Check if the Vault instance is sealed
i=0
if [[ "$SEALED" = "true" ]]; then
  info "Unsealing Vault..."
  for token in "${unseal_tokens[@]}"; do
    echo $token
    curl "$VAULT_ADDR/v1/sys/unseal" \
      -k \
      --silent \
      --key /usr/share/client-certs/AutoX_Client.key \
      --cert /usr/share/client-certs/AutoX_Client.crt \
      --request POST \
      --data '{"key":"'$token'"}'
  done
  if [[ $i -eq 3 ]]; then
    ok "Unsealing Vault completed"
  else
    error "Unsealing Vault Error"
  fi
else
  info "Vault is already unsealed."
fi
