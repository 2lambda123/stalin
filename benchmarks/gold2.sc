(define (abs1 x) (if (negative? x) (- x) x))
(define (abs2 x) (if (negative? x) (- x) x))
(define (abs3 x) (if (negative? x) (- x) x))
(define (abs4 x) (if (negative? x) (- x) x))
(define (abs5 x) (if (negative? x) (- x) x))
(define (abs6 x) (if (negative? x) (- x) x))

(define (e1 L) (car L))
(define (e2 L) (car (cdr L)))
(define (e3 L) (car (cdr (cdr L))))
(define (e4 L) (car (cdr (cdr (cdr L)))))
(define (e5 L) (car (cdr (cdr (cdr (cdr L))))))

(define (mnbrak F ax bx)
 (let ((gold 1.61803399)
       (glimit 100.0)
       (tiny 1.0e-20)
       (sign (lambda (x y) (if (< y 0.0) (- 0.0 x) x))))
  (define (loop ax fa bx fb cx fc)
   (if (< fb fc)
       (list ax fa bx fb cx fc)
       (let* ((r (* (- bx ax) (- fb fc)))
	      (q (* (- bx cx) (- fb fa)))
	      (ux (- bx (/ (- (* (- bx cx) q) (* (- bx ax) r))
			   (* (sign (max (abs1 (- q r)) tiny) (- q r)) 2.0))))
	      (uL (+ bx (* (- cx bx) glimit))))
	(if (> (* (- bx ux) (- ux cx)) 0.0)
	    (let ((fu (F ux)))
	     (if (< fu fc)
		 (list bx fb ux fu cx fc)
		 (if (> fu fb)
		     (list ax fa bx fb ux fu)
		     (let ((vx (+ cx (* (- cx bx) gold))))
		      (loop bx fb cx fc vx (F vx))))))
	    ;; (not (> (* (- bx) (- ux cx)) 0.0))
	    (if (> (* (- cx ux) (- ux uL)) 0.0)
		(let ((fu (F ux)))
		 (if (< fu fc)
		     (let ((vx (+ ux (* (- ux cx) gold))))
		      (loop cx fc ux fu vx (F vx)))
		     (loop bx fb cx fc ux fu)))
		(if (>= (* (- ux uL) (- uL cx)) 0.0)
		    (loop bx fb cx fc uL (F uL))
		    (let ((vx (+ cx (* (- cx bx) gold))))
		     (loop bx fb cx fc vx (F vx)))))))))
  (let ((fa (F ax))
	(fb (F bx)))
   (if (> fb fa)
       (let ((vx (+ ax (* (- ax bx) gold))))
	(loop bx fb ax fa vx (F vx)))
       (let ((vx (+ bx (* (- bx ax) gold))))
	(loop ax fa bx fb vx (F vx)))))))

(define (golden F tol ax bx cx)
 (let* ((gold 1.61803399)	; approximate
	(r (- gold 1.0))	; exact
	(c (- 1.0 r))		; exact
	(x1 bx)
	(x2 bx))
  (if (> (abs2 (- cx bx)) (abs3 (- bx ax)))
      (set! x2 (+ bx (* c (- cx bx))))
      (set! x1 (- bx (* c (- bx ax)))))
  (let loop ((x0 ax) (x1 x1) (f1 (F x1)) (x2 x2) (f2 (F x2)) (x3 cx))
   (if (<= (abs4 (- x3 x0)) (* tol (+ (abs5 x1) (abs6 x2))))
       (if (< f1 f2)
	   (list x1 f1)
	   (list x2 f2))
       (if (< f2 f1)
	   (let ((xn (+ (* r x2) (* c x3))))
	    (loop x1 x2 f2 xn (F xn) x3))
	   (let ((xn (+ (* r x1) (* c x0))))
	    (loop x0 xn (F xn) x1 f1 x2)))))))

(define (sqr x) (* x x))
(define (sqr1 x) (* x x))
(define (sqr2 x) (* x x))
(define (sqr3 x) (* x x))
(define (sqr4 x) (* x x))

(define (func x)
 (let ((a1 1.2)
       (a2 2.3)
       (a3 3.4)
       (a4 4.5))
  (+ (sqr1 (- x a1)) (sqr2 (- x a2)) (sqr3 (- x a3)) (sqr4 (- x a4)))))

(define (g L U)
 (let* ((bracket (mnbrak func L U))
	(best (golden func 1.0e-3 (e1 bracket) (e3 bracket) (e5 bracket))))
  (e2 best)))

(define (test)
 (let* ((n 1000)
	(eps (make-vector n 0.0))
	(sum 0.0))
  (do ((i 0 (+ i 1))) ((>= i n)) (vector-set! eps i (/ 1.0 (+ 2.0 i))))
  (do ((i 0 (+ i 1))) ((>= i n))
   (do ((j 0 (+ j 1))) ((>= j n))
    (set! sum (+ sum (g (- 1.0 (vector-ref eps i))
			(+ 5.0 (vector-ref eps j)))))))
  sum))

(begin (display (test)) (newline))
