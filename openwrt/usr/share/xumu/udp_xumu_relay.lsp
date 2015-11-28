(set 'port 9998)


(setq get_all_ip "ifconfig | grep \"inet addr:\" | sed -e 's/addr:/addr: /g' | awk '{print $3}'")
(setq all_ip_lst (exec get_all_ip))

(define (shot_to_dev buff tty)
	(setq cmd (string "echo '" buff "' > " tty))
	(! cmd)
)

(define (fork_to_listen ipaddr)
	(set 'listen (net-listen port ipaddr "udp"))
	(unless listen (begin
		(println "listening failed")
		(exit)))
	(while 
		(setq pkg (net-receive-from listen 128) )
		(println pkg)
		(shot_to_dev (pkg 0) "/dev/ttyATH0")
		(sleep 300)
	)
	(net-close listen)
)
(dolist (x all_ip_lst)
	(if (and (!= x "127.0.0.1") (not (starts-with x "11.10")))
		(fork (fork_to_listen x))
	)
	(sleep 500)
)

