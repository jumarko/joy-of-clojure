(ns joy-of-clojure.ch04.core)

;;; Chapter 04 - On scalars


(let [imadeuapi 3.14159265358979123981239719283719823798172389M]
  (println (class imadeuapi))
  imadeuapi)

;; VS following where the number is truncated
(let [imadeuapi 3.14159265358979123981239719283719823798172389]
  (println (class imadeuapi))
  imadeuapi)

;; promotion
(def my-number 9)
(class my-number);=> Long
(class (+ my-number 900000000000000000000));=> BigInit

;; overflow
;; - in most cases not an issue thanks to promotion
;; - when operating on primitive types, exception is thrown
(+ Long/MAX_VALUE Long/MAX_VALUE);=> ArithmeticException

;; underflow - number is so small that its value collapses into zero
;; - occurs only with floating-point numbers
(float 0.00000000000000000000000000000000000000000000000000001)
1.0E-430

;; Rounding erros - Patriot missile
(let [approx-interval (/ 209715 2097152); Patriot's approx 0.1
      actual-interval (/ 1 10); Clojure can accurately represent 0.1
      hours (* 3600 100 10)
      actual-total (double (* hours actual-interval))
      approx-total (double (* hours approx-interval))]
  (- actual-total approx-total))

;; One way to contribute to rounding erros is to introduce doubles and floats into an operation
(+ 0.1 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M)

;; Rational numbers
;; - BigDecimal is finite in that it uses a 32-bit integer to represent number of digits to the right of the decimal place -> subjet to error
1.0E-430000000M
;; VS
;1.0E-4300000000M;=> NumberFormatException

;; associativeness is broken!
(def a 1.0e50)
(def b -1.0e50)
(def c 17.0e00)
(+ (+ a b) c);=> 17.0
(+ a (+ b c));=> 0.0

;; and fixed
(def a (rationalize 1.0e50))
(def b (rationalize -1.0e50))
(def c (rationalize 17.0e00))
(+ (+ a b) c)
(+ a (+ b c))

(numerator (/ 123 10))
(denominator (/ 123 10))


;;; Keywords
:a-keyword
; qualified keyword
::also-a-keyword;=>:joy-of-clojure.ch04.core/also-a-keyword

;; keywords are almost always used as map keys
(def population {:zombies 2700, :humans 5})
(println (/ (:zombies  population)
            ( :humans  population))
         "zombies per capita")

;; keywords can be used as enumerations, as multimethod dispatch values and as directives
(defn pour [lb ub]
  (cond
   (= ub :toujours) (iterate inc lb)
   :else (range lb ub)) )


;;; symbols
(identical? 'goat 'goat);=> false
(= 'goat 'goat);=> true
(name 'goat)
(let [x 'goat y x]
  (identical? x y))

;; equally named symbols often aren't the same instance because each can have its own unique metadata
(let [x (with-meta 'goat {:ornery false})
      y (with-meta 'goat {:ornery true})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])

;; but two equally named keywords are identical
(identical? :goat :goat)
;; and therefore they can't hold metadata
(with-meta ':goat {:ornery false})

;; clojure is a Lisp-1 - same name resolution for function and value bindings
(defn best [f xs]
  (reduce #(if (f %1 %2) %1 %2) xs))

(best > [1 3 4 2 7 5 3])


;;; Regular expressions
(class #"an example pattern")
;; backslashes don't have to be doubled
(java.util.regex.Pattern/compile "\\d");=> #"\d"
;; usefull flags
#"(?i) I am Case Insensitive"
#"(?m)I\n am \n multiline"

;; re-seq functional is the one you usually want
(re-seq #"\w+" "one-two/three ")
(re-seq #"\w*(\w)(\w)" "one-two/three ")

;; Beware that Matcher object is mutable!
;; - it's exposed via re-matcher; re-find and re-groups use it
