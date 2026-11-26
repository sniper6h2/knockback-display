#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EAGER_MOD_ROOT="$ROOT" exec "$ROOT/eagerlib/testing/smoke-test.sh" "$@"
