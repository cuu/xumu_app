
(setq port 9999)

(setq hmac (crc32 (read-file "/usr/share/hmac.txt")))

(define (mac)
 (string "{\"zigbee_device\":\"" hmac "\"}")
)

(define (broadcast ip )
	(println "broadcast: " ip )
	(net-send-udp ip port (mac) true )
)


(setq get_all_bcast "ifconfig | grep \"Bcast:\" | cut -d : -f3 | cut -d \" \" -f1")

(define (bcast_self_ip)
	(setq all_ip_lst (exec  get_all_bcast))
	(dolist (x all_ip_lst)
		(broadcast x)
		(sleep 500)
	)
)

(while true
	(bcast_self_ip)
	(sleep 1000)
)

(exit)

