#!/bin/bash
socat - unix-client:/tmp/ath0 | newlisp /usr/share/xumu/socat_xumu_stdin.lsp


