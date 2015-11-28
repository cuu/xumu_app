(load "/usr/share/json.lsp")

(constant 'port 9999)

(setq get_all_bcast "ifconfig | grep \"Bcast:\" | cut -d : -f3 | cut -d \" \" -f1")

(define (broadcast ip msg )
	(println "socat xumu stdio broadcast: " ip )
	(net-send-udp ip port msg true )
)

(define (bcast_self_msg msg)
	(setq all_ip_lst (exec  get_all_bcast))
	(dolist (x all_ip_lst)
		(broadcast x msg)
		(sleep 500)
	)
)

(while (read-line)
	(bcast_self_msg (string (current-line) "\n"))
)
