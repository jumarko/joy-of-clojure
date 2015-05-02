(ns joy-of-clojure.ch02.core)

;;; Examples from chapter 2 (Drinking from the Clojure fire hose) of book Joy of Clojure
;;;

;;; 2.8 Using hosting libraries via interopt
;;; ========================================

;; Accessing static class members
java.util.Locale/JAPAN
(Math/sqrt 9)

;; Creating instances
(new java.awt.Point 0 1)
(java.awt.Point. 0 1)
(java.util.HashMap. {"foo" 42 "bar" 9 "baz" "quux"})

;; Accessing instance members
(.-x (java.awt.Point. 10 20))
;; simple ".x" works as well
(.x (java.awt.Point. 10 20))
(.divide (java.math.BigDecimal. "42") 2M)

;; setting instance fields
(let  [origin (java.awt.Point. 0 0)]
     (set! (.x origin) 15)
     (str origin))

;; .. macro
;; consider following java code:
;; new java.util.Date().toString().endsWith("2014")
(.endsWith (.toString (java.util.Date.)) "2014")
(.. (java.util.Date.) toString (endsWith "2014") )

;; doto macro
(doto (java.util.HashMap.)
  (.put "HOME" "/home/me")
  (.put "SRC" "src")
  (. put "BIN" "classes"))
