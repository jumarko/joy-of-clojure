(ns joy-of-clojure.ch03.core)

;;; Chapter 3 - Dipping your toes in the pool

;;; 3.1 Truthiness
;;; ==============

;; Every value looks like truth except the false and nil
(if true :truthy :falsey)
(if [] :truthy :falsey)
(if nil :truthy :falsey)
(if false :truthy :falsey)

;; Never do this!!
(def evil-false (Boolean. "false"))
evil-false
(= false evil-false)
(if evil-false :truthy :falsey)

;; if you need to distinguish between nil and false
(when (nil? nil) "Actually nil, not false")
(false? false)
(false? nil)


;;; 3.2 Nil pun with care
;;; =====================

;; Testing if collection is empty
(seq [1 2 3])
(seq []) ;=> nil
(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))
(print-seq [])
(print-seq [1 2])


;;; 3.3 Destructuring
;;; =================

(defn whole-name-vector [[f-name m-name l-name] & args]
  (str l-name ", " f-name " " m-name))
(whole-name-vector ["Guy" "Lewis" "Steele"])

(defn whole-name-vector-args [ & args]
  (let [[f-name m-name l-name] args]
    (str l-name ", " f-name " " m-name)))
(whole-name-vector-args "Guy" "Lewis" "Steele")

(defn whole-name-map [{:keys [f-name m-name l-name]}]
  (str l-name ", " f-name " " m-name))
(whole-name-map {:f-name "Guy" :m-name "Lewis" :l-name "Steele"})

(defn whole-name-map-args [& args]
  (let [{:keys [f-name m-name l-name]} args]
    (str l-name ", " f-name " " m-name)))
(whole-name-map-args :f-name "Guy" :m-name "Lewis" :l-name "Steele")


;;; 3.4 Using REPL to experiment
;;; ============================

(range 5)
(for [x (range 2) y (range 2)]
  [x y (bit-xor x y)])
;; wrapping in a function
(defn xors [max-x max-y]
  (for [x (range max-x) y (range max-y)]
    [x y (rem (bit-xor x y) 256  )]))

;; Graphics


(def frame (java.awt.Frame. ))
;; no window appears
frame
;; find method for setting visibility
(for [meth (.getMethods java.awt.Frame)
      :let [name (.getName meth)]
      :when (re-find #"Vis" name)]
  name)

(defn find-class-methods [my-class method-pattern]
  "Find all methods on given class matching given pattern (case insensitive matching).
   Parameter types (if any) are show as well."
  (for [meth (.getMethods my-class)
      :let [name (.getName meth)]
      :when (re-find (re-pattern (str "(?i)" method-pattern)) name)]
    [name
     (map #(.getName %) (.getParameterTypes meth)) ] ))

(defn find-methods [my-object method-pattern]
  (find-class-methods (.getClass my-object) method-pattern)
)

(find-methods (.getClass frame) "size")
(.setSize frame 500 500)
;; Check http://stackoverflow.com/questions/28018850/window-does-not-appear-using-clojure-on-mac-os-x if you can't see the window
(.setVisible frame true)

(def gfx (.getGraphics frame))
(find-methods gfx "fill")
(.fillRect gfx 100 100 50 75)
(.setColor gfx (java.awt.Color. 255 128 0))
(.fillRect gfx 100 150 75 50)

;; Can we try larger window?
;; Notice, that original xors function had to be modified to use module (rem)
(doseq [[x y xor] (xors 500 500)]
  (.setColor gfx (java.awt.Color. xor xor xor))
  (.fillRect gfx x y 1 1))

;; Clear graphis
(defn clear [g] (.clearRect g 0 0 200 200))
(clear gfx)

(defn f-values [f xs ys]
  (for [x (range xs) y (range ys)]
    [x y (rem (f x y) 256  )]))


(defn draw-values [f xs ys]
  (clear gfx)
  (.setSize frame xs ys)
  (doseq [[x y v] (f-values f xs ys)]
    (.setColor gfx (java.awt.Color. v v v))
    (.fillRect gfx x y 1 1)))

(draw-values bit-and 256 256)
(draw-values + 256 256)
(draw-values * 256 256)
(draw-values #(Math/abs (- %1 %2) ) 256 256)
