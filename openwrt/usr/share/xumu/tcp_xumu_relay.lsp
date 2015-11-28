(set 'port 1234)
(set 'listen (net-listen port))
(unless listen (begin
    (print "listening failed\n")
    (exit)))

(print "Waiting for connection on: " port "\n")

(define (shot_to_socat buff unix_socket)
	(exec (string "echo -en " buff " | socat STDIO unix-client:" unix_socket))
)

(define (shot_to_dev buff tty) 
	(setq cmd (string "echo  '" buff "' > " tty))
	(println cmd)
	(! cmd)
) 

(while true
	(set 'connection (net-accept listen))
	(if connection
    	(while (net-receive connection buff 1024 "\n")
        (setq buff (chop buff))
				;(shot_to_socat buff "/tmp/ath0")
				(shot_to_dev buff "/dev/ttyATH0")
        
		)
    	(print "Could not connect\n")
	)

)
