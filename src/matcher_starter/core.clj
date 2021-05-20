(ns matcher-starter.core
          (:require [org.clojars.cognesence.breadth-search.core :refer :all]
                    [org.clojars.cognesence.matcher.core :refer :all]
                    [org.clojars.cognesence.ops-search.core :refer :all]))

(defn tester [tests]                                        ;prints any test that fails, no output if all successful
  (doseq [test tests]
    (mif ['(?id -- ?pre => ?post) test]
         (when-not (= (eval (? pre)) (? post))
           (println 'Test 'Failed '- 'Id (? id) '-- 'Actual (eval(? pre))  '=> 'Expected  (? post))))))

(def world
  '#{(zone zone1)
     (zone zone2)
     (exchange exc1)
     (corridor cor1)
     (in cor1 zone1)
     (attaches cor1 exc1)
     (attaches exc1 cor1)
     (attaches bay1 cor1)
     (attaches cor1 bay1)
     (attaches bay2 cor1)
     (attaches cor1 bay2)
     (attaches bay3 cor1)
     (attaches cor1 bay3)
     (attaches bay4 cor1)
     (attaches cor1 bay4)
     (corridor cor2)
     (in cor2 zone2)
     (attaches cor2 exc1)
     (attaches exc1 cor2)
     (attaches bay5 cor2)
     (attaches cor2 bay5)
     (attaches bay6 cor2)
     (attaches cor2 bay6)
     (attaches bay7 cor2)
     (attaches cor2 bay7)
     (attaches bay8 cor2)
     (attaches cor2 bay8)
     (junction junct1)
     (attaches cor1 junct1)
     (attaches junct1 cor1)
     (junction junct2)
     (attaches cor2 junct2)
     (attaches junct2 cor2)
     (corridor cor3)
     (attaches cor3 junct1)
     (attaches junct1 cor3)
     (attaches cor3 bay9)
     (attaches bay9 cor3)
     (attaches cor3 bay10)
     (attaches bay10 cor3)
     (attaches cor3 bay11)
     (attaches bay11 cor3)
     (attaches cor3 bay12)
     (attaches bay12 cor3)
     (corridor cor4)
     (attaches cor4 junct2)
     (attaches junct2 cor4)
     (attaches cor4 bay13)
     (attaches bay13 cor4)
     (attaches cor4 bay14)
     (attaches bay14 cor4)
     (attaches cor4 bay15)
     (attaches bay15 cor4)
     (attaches cor4 bay16)
     (attaches bay16 cor4)
     (corridor cor5)
     (attaches cor5 junct1)
     (attaches junct1 cor5)
     (attaches cor5 bay17)
     (attaches bay17 cor5)
     (attaches cor5 bay18)
     (attaches bay18 cor5)
     (attaches cor5 bay19)
     (attaches bay19 cor5)
     (attaches cor5 bay20)
     (attaches bay20 cor5)
     (corridor cor6)
     (attaches cor6 junct2)
     (attaches junct2 cor6)
     (attaches cor6 bay20)
     (attaches bay20 cor6)
     (attaches cor6 bay21)
     (attaches bay21 cor6)
     (attaches cor6 bay22)
     (attaches bay22 cor6)
     (attaches cor6 bay23)
     (attaches bay23 cor6)
     (corridor cor7)
     (attaches cor7 junct1)
     (attaches junct1 cor7)
     (attaches cor7 bay24)
     (attaches bay24 cor7)
     (attaches cor7 bay25)
     (attaches bay25 cor7)
     (attaches cor7 bay26)
     (attaches bay26 cor7)
     (attaches cor7 bay27)
     (attaches bay27 cor7)
     (corridor cor8)
     (attaches cor8 junct2)
     (attaches junct2 cor8)
     (attaches cor8 bay28)
     (attaches bay28 cor8)
     (attaches cor8 bay29)
     (attaches bay29 cor8)
     (attaches cor8 bay30)
     (attaches bay30 cor8)
     (attaches cor8 bay31)
     (attaches bay31 cor8)
     })

(def state
  '#{(car car1)
     (in car1 zone1)
     (at car1 bay3)
     (carries car1 nil)
     (car car2)
     (in car2 zone2)
     (at car2 bay29)
     (carries car2 nil)
     (orientation car1 vertical)
     (orientation car2 horizontal)
     (stock stock1)
     (stock stock2)
     (orientation stock1 vertical)
     (orientation stock2 horizontal)
     (at stock1 bay6)
     (at stock2 bay15)
     })

(def ops
  '{collect-stock-from-bay {:pre ((car ?car)
                                  (bay ?bay)
                                  (stock ?stock)
                                  (at ?car ?bay)
                                  (at ?stock ?bay)
                                  (carries ?car nil)
                                  )
                            :add ((carries ?car ?stock))
                            :del  ((at ?stock ?bay)
                                   (carries ?car nil))
                            :txt   (collect ?stock from ?bay to ?car)
                            :cmd []
                            }
    collect-stock-from-exchange {:pre ((car ?car)
                                       (exchange ?exc)
                                       (stock ?stock)
                                       (at ?car ?exc)
                                       (at ?stock ?exc)
                                       (carries ?car nil)
                                       )
                                 :add ((carries ?car ?stock)
                                       (at nil ?exc))
                                 :del  ((at ?stock ?exc)
                                        (carries ?car nil))
                                 :txt  (collect ?stock from ?exc to ?car)
                                 :cmd []
                                 }
    deposit-stock-to-bay {:pre ((car ?car)
                                (bay ?bay)
                                (stock ?stock)
                                (at ?car ?bay)
                                (carries ?car ?stock)
                                )
                          :add ((carries ?car nil)
                                (at ?stock ?bay))
                          :del  ((carries ?car ?stock))
                          :txt   (deposit ?stock from ?car to ?bay)
                          :cmd []
                          }
    deposit-stock-to-exchange {:pre ((exchange ?exc)
                                     (car ?car)
                                     (stock ?stock)
                                     (at nil ?exc)
                                     (at ?car ?exc)
                                     (carries ?car ?stock)
                                     )
                               :add ((carries ?car nil)
                                     (at ?stock ?exc))
                               :del  ((carries ?car ?stock)
                                      (at nil ?exc))
                               :txt   (deposit ?stock from ?car to ?exc)
                               :cmd []
                               }
    move-from-corridor-to-bay {:pre ((car ?car)
                                     (corridor ?cor)
                                     (bay ?bay)
                                     (at ?car ?cor)
                                     (attaches ?cor ?bay)
                                     )
                               :add ((at ?car ?bay))
                               :del  ((at ?car ?cor))
                               :txt   (move ?car from ?cor to ?bay)
                               :cmd []
                               }
    move-from-bay-to-corridor {:pre ((car ?car)
                                     (corridor ?cor)
                                     (bay ?bay)
                                     (at ?car ?bay)
                                     (attaches ?cor ?bay)
                                     )
                               :add ((at ?car ?cor))
                               :del  ((at ?car ?bay))
                               :txt   (move ?car from ?bay to ?cor)
                               :cmd []
                               }
    move-from-corridor-to-junction {:pre ((car ?car)
                                          (corridor ?cor)
                                          (junction ?junct)
                                          (attaches ?junct ?cor)
                                          (at ?car ?cor)
                                          )
                                    :add ((at ?car ?junct))
                                    :del  ((at ?car ?cor))
                                    :txt   (move ?car from ?cor to ?junct)
                                    :cmd []
                                    }
    move-from-junction-to-corridor-no-stock {:pre ((car ?car)
                                                   (corridor ?cor)
                                                   (junction ?junct)
                                                   (carries ?car nil)
                                                   (attaches ?junct ?cor)
                                                   (at ?car ?junct))
                                             :add ((at ?car ?cor))
                                             :del  ((at ?car ?junct))
                                             :txt   (move ?car from ?junct to ?cor)
                                             :cmd []
                                             }
    move-from-junction-to-corridor-has-stock {:pre ((car ?car)
                                                    (stock ?stock)
                                                    (corridor ?cor)
                                                    (junction ?junct)
                                                    (attaches ?junct ?cor)
                                                    (carries ?car ?stock)
                                                    (orientation ?stock ?orientation)
                                                    (orientation ?cor ?orientation)
                                                    (at ?car ?junct))
                                              :add ((at ?car ?cor))
                                              :del  ((at ?car ?junct))
                                              :txt   (move ?car from ?junct to ?cor)
                                              :cmd []
                                              }
    move-from-corridor-to-exchange {:pre ((car ?car)
                                          (corridor ?cor)
                                          (exchange ?exc)
                                          (at ?car ?cor)
                                          (attaches ?cor ?exc))
                                    :add ((at ?car ?exc))
                                    :del  ((at ?car ?cor))
                                    :txt   (move ?car from ?cor to ?exc)
                                    :cmd []
                                    }
    move-from-exchange-to-corridor {:pre ((car ?car)
                                          (corridor ?cor)
                                          (exchange ?exc)
                                          (zone ?zone)
                                          (in ?cor ?zone)
                                          (in ?car ?zone)
                                          (at ?car ?exc)
                                          (attaches ?cor ?exc))
                                    :add ((at ?car ?cor))
                                    :del  ((at ?car ?exc))
                                    :txt   (move ?car from ?exc to ?cor)
                                    :cmd []
                                    }
    rotate-car-ns-to-ew {:pre ((car ?car)
                               (corridor ?cor)
                               (stock ?stock)
                               (carries ?car ?stock)
                               (junction ?junct)
                               (attaches ?junct ?cor)
                               (at ?car ?junct)
                               (orientation ?stock north-south)
                               (orientation ?cor east-west))
                         :add ((orientation ?stock east-west))
                         :del  ((orientation ?stock north-south))
                         :txt   (rotate ?car at ?junct from north-south to east-west)
                         :cmd []
                         }
    rotate-car-ew-to-ns {:pre ((car ?car)
                               (corridor ?cor)
                               (stock ?stock)
                               (carries ?car ?stock)
                               (junction ?junct)
                               (attaches ?junct ?cor)
                               (at ?car ?junct)
                               (orientation ?stock east-west)
                               (orientation ?cor north-south))
                         :add ((orientation ?stock north-south))
                         :del  ((orientation ?stock east-west))
                         :txt   (rotate ?car at ?junct from east-west to north-south)
                         :cmd []
                         }

    })

(def world-1
  '#{(bay bay-1)
     (car car-1)
     (stock stock-1)})

(def start-state-1
  '#{(at car-1 bay-1)
     (carries car-1 nil)
     (at stock-1 bay-1)})

(def world-2
  '#{(exchange exc-1)
     (car car-1)
     (stock stock-1)})

(def start-state-2
  '#{(at car-1 exc-1)
     (carries car-1 nil)
     (at stock-1 exc-1)})

(def world-3
  '#{(bay bay-1)
     (car car-1)
     (stock stock-1)})

(def start-state-3
  '#{(at car-1 bay-1)
     (carries car-1 stock-1)})

(def world-4
  '#{(exchange exc-1)
     (car car-1)
     (stock stock-1)})

(def start-state-4
  '#{(at car-1 exc-1)
     (carries car-1 stock-1)
     (at nil exc-1)})

(def world-5
  '#{(bay bay-1)
     (corridor corridor-1)
     (attaches corridor-1 bay-1)
     (car car-1)})

(def start-state-5
  '#{(at car-1 corridor-1)})

(def world-6
  '#{(bay bay-1)
     (corridor corridor-1)
     (attaches corridor-1 bay-1)
     (car car-1)})

(def start-state-6
  '#{(at car-1 bay-1)})

(def world-7
  '#{(junction junction-1)
     (corridor corridor-1)
     (attaches junction-1 corridor-1)
     (car car-1)})

(def start-state-7
  '#{(at car-1 corridor-1)})

(def world-8
  '#{(junction junction-1)
     (corridor corridor-1)
     (attaches junction-1 corridor-1)
     (car car-1)
     (carries car-1 nil)})

(def start-state-8
  '#{(at car-1 junction-1)})

(def world-9
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (attaches junction-1 corridor-1)
     (orientation corridor-1 north-south)
     })

(def start-state-9
  '#{(at car-1 junction-1)
     (orientation stock-1 north-south)})

(def world-10
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (attaches junction-1 corridor-1)
     (orientation corridor-1 north-south)
     })

(def start-state-10
  '#{(at car-1 junction-1)
     (orientation stock-1 east-west)})

(def world-11
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (attaches junction-1 corridor-1)
     (orientation corridor-1 east-west)
     })

(def start-state-11
  '#{(at car-1 junction-1)
     (orientation stock-1 north-south)})

(def world-12
  '#{(car car-1)
     (exchange exchange-1)
     (corridor corridor-1)
     (attaches corridor-1 exchange-1)
     })

(def start-state-12
  '#{(at car-1 corridor-1)})

(def world-13
  '#{(car car-1)
     (corridor corridor-1)
     (exchange exchange-1)
     (attaches corridor-1 exchange-1)
     (zone zone-1)
     (in car-1 zone-1)
     (in corridor-1 zone-1)
     })

(def start-state-13
  '#{(at car-1 exchange-1)})

(def world-14
  '#{(car car-1)
     (junction junction-1)
     (corridor corridor-1)
     (attaches junction-1 corridor-1)
     (at car-1 junction-1)
     (orientation corridor-1 north-south)
     (stock stock-1)
     (carries car-1 stock-1)
     })

(def start-state-14
  '#{(orientation stock-1 east-west)})

(def world-15
  '#{(car car-1)
     (junction junction-1)
     (corridor corridor-1)
     (attaches junction-1 corridor-1)
     (at car-1 junction-1)
     (orientation corridor-1 east-west)
     (stock stock-1)
     (carries car-1 stock-1)
     })

(def start-state-15
  '#{(orientation stock-1 north-south)})

(def world-31
  '#{(bay bay-1)
     (car car-1)
     (stock stock-1)
     (stock stock-2)})

(def start-state-31
  '#{(at car-1 bay-1)
     (carries car-1 stock-1)
     (at stock-2 bay-1)})

(def world-35
  '#{(bay bay-1)
     (corridor corridor-1)
     (attaches corridor-1 bay-1)
     (car car-1)})

(def start-state-35
  '#{(at car-1 bay-1)
     (carries car-1 nil)})

(def single-state-tests
  ;collect-stock-from-bay
  '( (1 -- (ops-search start-state-1 '((carries car-1 stock-1)) ops :world world-1) =>
       {:state #{(at car-1 bay-1) (carries car-1 stock-1)},
        :path (#{(at car-1 bay-1) (at stock-1 bay-1) (carries car-1 nil)}),
        :cmds ([]),
        :txt ((collect stock-1 from bay-1 to car-1))})
    ;collect-stock-from-exchange
    (2 -- (ops-search start-state-2 '((carries car-1 stock-1)) ops :world world-2) =>
      {:state #{(at car-1 exc-1) (at nil exc-1) (carries car-1 stock-1)},
       :path (#{(at car-1 exc-1) (at stock-1 exc-1) (carries car-1 nil)}),
       :cmds ([]),
       :txt ((collect stock-1 from exc-1 to car-1))})
    ;deposit-stock-to-bay
    (3 -- (ops-search start-state-3 '((at stock-1 bay-1)) ops :world world-3) =>
      {:state #{(at car-1 bay-1) (at stock-1 bay-1) (carries car-1 nil)},
       :path (#{(at car-1 bay-1) (carries car-1 stock-1)}),
       :cmds ([]),
       :txt ((deposit stock-1 from car-1 to bay-1))})
    ;deposit-stock-to-exchange
    (4 -- (ops-search start-state-4 '((at stock-1 exc-1)) ops :world world-4) =>
      {:state #{(at car-1 exc-1) (at stock-1 exc-1) (carries car-1 nil)},
       :path (#{(at car-1 exc-1) (at nil exc-1) (carries car-1 stock-1)}),
       :cmds ([]),
       :txt ((deposit stock-1 from car-1 to exc-1))})
    ;move-from-corridor-to-bay
    (5 -- (ops-search start-state-5 '((at car-1 bay-1)) ops :world world-5) =>
      {:state #{(at car-1 bay-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to bay-1))})
    ;move-from-bay-to-corridor
    (6 -- (ops-search start-state-6 '((at car-1 corridor-1)) ops :world world-6) =>
      {:state #{(at car-1 corridor-1)},
       :path (#{(at car-1 bay-1)}),
       :cmds ([]),
       :txt ((move car-1 from bay-1 to corridor-1))})
    ;move-from-corridor-to-junction
    (7 -- (ops-search start-state-7 '((at car-1 junction-1)) ops :world world-7) =>
      {:state #{(at car-1 junction-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to junction-1))})
    ;move-from-junction-to-corridor-no-stock
    (8 -- (ops-search start-state-8 '((at car-1 corridor-1)) ops :world world-8) =>
      {:state #{(at car-1 corridor-1)},
       :path (#{(at car-1 junction-1)}),
       :cmds ([]),
       :txt ((move car-1 from junction-1 to corridor-1))})
    ;move-from-junction-to-corridor-has-stock, stock is already aligned
    (9 -- (ops-search start-state-9 '((at car-1 corridor-1)) ops :world world-9) =>
      {:state #{(at car-1 corridor-1) (orientation stock-1 north-south)},
       :path (#{(at car-1 junction-1) (orientation stock-1 north-south)}),
       :cmds ([]),
       :txt ((move car-1 from junction-1 to corridor-1))})
    ;move-from-junction-to-corridor-has-stock, stock is not aligned and currently east-west
    (10 -- (ops-search start-state-10 '((at car-1 corridor-1)) ops :world world-10) =>
      {:state #{(at car-1 corridor-1)
                (orientation stock-1 north-south)},
       :path (#{(at car-1 junction-1)(orientation stock-1 east-west)}
              #{(at car-1 junction-1)(orientation stock-1 north-south)}),
       :cmds ([] []),
       :txt ((rotate car-1 at junction-1 from east-west to north-south)(move car-1 from junction-1 to corridor-1)
             )})
    ;move-from-junction-to-corridor-has-stock, stock is not aligned and currently north-south
    (11 -- (ops-search start-state-11 '((at car-1 corridor-1)) ops :world world-11) =>
      {:state #{(at car-1 corridor-1)
                (orientation stock-1 east-west)},
       :path (#{(at car-1 junction-1)(orientation stock-1 north-south)}
              #{(at car-1 junction-1)(orientation stock-1 east-west)}),
       :cmds ([] []),
       :txt ((rotate car-1 at junction-1 from north-south to east-west)(move car-1 from junction-1 to corridor-1)
             )})
    ;move-from-corridor-to-exchange
    (12 -- (ops-search start-state-12 '((at car-1 exchange-1)) ops :world world-12) =>
      {:state #{(at car-1 exchange-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to exchange-1)
             )})
    ;move-from-exchange-to-corridor
    (13 -- (ops-search start-state-13 '((at car-1 corridor-1)) ops :world world-13) =>
      {:state #{(at car-1 corridor-1)},
       :path (#{(at car-1 exchange-1)}),
       :cmds ([]),
       :txt ((move car-1 from exchange-1 to corridor-1)
             )})
    ;rotate-car from east-west to north-south
    (14 -- (ops-search start-state-14 '((orientation stock-1 north-south)) ops :world world-14) =>
      {:state #{(orientation stock-1 north-south)},
       :path (#{(orientation stock-1 east-west)}),
       :cmds ([]),
       :txt ((rotate car-1 at junction-1 from east-west to north-south)
             )})
    ;rotate-car from north-south to east-west
    (15 -- (ops-search start-state-15 '((orientation stock-1 east-west)) ops :world world-15) =>
      {:state #{(orientation stock-1 east-west)},
       :path (#{(orientation stock-1 north-south)}),
       :cmds ([]),
       :txt ((rotate car-1 at junction-1 from north-south to east-west)
             )})
    ;deposit-stock-to-bay that already has stock
    (31 -- (ops-search start-state-31 '((at stock-1 bay-1)) ops :world world-31) =>
      {:state #{(at car-1 bay-1) (at stock-1 bay-1) (at stock-2 bay-1) (carries car-1 nil)},
       :path (#{(at car-1 bay-1) (at stock-2 bay-1) (carries car-1 stock-1)}),
       :cmds ([]),
       :txt ((deposit stock-1 from car-1 to bay-1))})
    ;more from bay to corridor
    (48 -- (ops-search start-state-35 '((at car-1 corridor-1)) ops :world world-35) =>
      {:state #{(carries car-1 nil) (at car-1 corridor-1)},
       :path (#{(at car-1 bay-1) (carries car-1 nil)}),
       :cmds ([]),
       :txt ((move car-1 from bay-1 to corridor-1))})

    ))

(def world-16
  '#{(bay bay-1)
     (car car-1)
     (stock stock-1)})

(def start-state-16
  '#{(at car-1 bay-1)
     (carries car-1 nil)})

(def world-17
  '#{(exchange exc-1)
     (car car-1)
     (stock stock-1)})

(def start-state-17
  '#{(at car-1 exc-1)
     (carries car-1 nil)})

(def world-18
  '#{(bay bay-1)
     (car car-1)
     (stock stock-1)})

(def start-state-18
  '#{(at car-1 bay-1)})

(def world-19
  '#{(exchange exc-1)
     (car car-1)
     (stock stock-1)})

(def start-state-19
  '#{(at car-1 exc-1)
     (at nil exc-1)})

(def world-20
  '#{(bay bay-1)
     (corridor corridor-1)
     (car car-1)})

(def start-state-20
  '#{(at car-1 corridor-1)})

(def world-21
  '#{(bay bay-1)
     (corridor corridor-1)
     (car car-1)})

(def start-state-21
  '#{(at car-1 bay-1)})

(def world-22
  '#{(junction junction-1)
     (corridor corridor-1)
     (car car-1)})

(def start-state-22
  '#{(at car-1 corridor-1)})

(def world-23
  '#{(junction junction-1)
     (corridor corridor-1)
     (car car-1)
     (carries car-1 nil)})

(def start-state-23
  '#{(at car-1 junction-1)})

(def world-24
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (orientation corridor-1 north-south)
     })

(def start-state-24
  '#{(at car-1 junction-1)
     (orientation stock-1 north-south)})

(def world-25
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (orientation corridor-1 north-south)
     })

(def start-state-25
  '#{(at car-1 junction-1)
     (orientation stock-1 east-west)})

(def world-26
  '#{(car car-1)
     (stock stock-1)
     (carries car-1 stock-1)
     (corridor corridor-1)
     (junction junction-1)
     (orientation corridor-1 east-west)
     })

(def start-state-26
  '#{(at car-1 junction-1)
     (orientation stock-1 north-south)})

(def world-27
  '#{(car car-1)
     (exchange exchange-1)
     (corridor corridor-1)
     })

(def start-state-27
  '#{(at car-1 corridor-1)})

(def world-28
  '#{(car car-1)
     (corridor corridor-1)
     (exchange exchange-1)
     (zone zone-1)
     (in car-1 zone-1)
     (in corridor-1 zone-1)

     })

(def start-state-28
  '#{(at car-1 exchange-1)})

(def world-29
  '#{(car car-1)
     (junction junction-1)
     (corridor corridor-1)
     (at car-1 junction-1)
     (orientation corridor-1 north-south)
     (stock stock-1)
     })

(def start-state-29
  '#{(orientation stock-1 east-west)})

(def world-30
  '#{(car car-1)
     (junction junction-1)
     (corridor corridor-1)
     (attaches junction-1 corridor-1)
     (at car-1 junction-1)
     (orientation corridor-1 east-west)
     (stock stock-1)
     })

(def start-state-30
  '#{(orientation stock-1 north-south)})

(def world-32
  '#{(exchange exc-1)
     (car car-1)
     (stock stock-1)
     (stock stock-2)})

(def start-state-32
  '#{(at car-1 exc-1)
     (at stock-2 exc-1)
     (carries car-1 stock-1)})

(def world-33
  '#{(car car-1)
     (corridor corridor-1)
     (exchange exchange-1)
     (attaches corridor-1 exchange-1)
     (zone zone-1)
     (zone zone-2)
     (in car-1 zone-1)
     (in corridor-1 zone-2)
     })

(def start-state-33
  '#{(at car-1 exchange-1)})

(def illegal-operation-tests
  ;collect-stock-from-bay with no stock
  '( (16 -- (ops-search start-state-16 '((carries car-1 stock-1)) ops :world world-16) => nil)
    ;collect-stock-from-exchange with no stock
    (17 -- (ops-search start-state-17 '((carries car-1 stock-1)) ops :world world-17) => nil)
    ;deposit-stock-to-bay when car has no stock
    (18 -- (ops-search start-state-18 '((at stock-1 bay-1)) ops :world world-18) => nil)
    ;deposit-stock-to-exchange when car has no stock
    (19 -- (ops-search start-state-19 '((at stock-1 exc-1)) ops :world world-19) => nil)
    ;move-from-corridor-to-bay when they are not connected
    (20 -- (ops-search start-state-20 '((at car-1 bay-1)) ops :world world-20) => nil)
    ;move-from-bay-to-corridor when they are not connected
    (21 -- (ops-search start-state-21 '((at car-1 corridor-1)) ops :world world-21) => nil)
    ;move-from-corridor-to-junction when they are not connected
    (22 -- (ops-search start-state-22 '((at car-1 junction-1)) ops :world world-22) => nil)
    ;move-from-junction-to-corridor-no-stock  when they are not connected
    (23 -- (ops-search start-state-23 '((at car-1 corridor-1)) ops :world world-23) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is already aligned
    (24 -- (ops-search start-state-24 '((at car-1 corridor-1)) ops :world world-24) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is not aligned and currently east-west
    (25 -- (ops-search start-state-25 '((at car-1 corridor-1)) ops :world world-25) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is not aligned and currently north-south
    (26 -- (ops-search start-state-26 '((at car-1 corridor-1)) ops :world world-26) => nil)
    ;move-from-corridor-to-exchange when they are not connected
    (27 -- (ops-search start-state-27 '((at car-1 exchange-1)) ops :world world-27) => nil)
    ;move-from-exchange-to-corridor when they are not connected
    (28 -- (ops-search start-state-28 '((at car-1 corridor-1)) ops :world world-28) => nil)
    ;rotate-car from east-west to north-south when carries no stock
    (29 -- (ops-search start-state-29 '((orientation stock-1 north-south)) ops :world world-29) => nil)
    ;rotate-car from north-south to east-west when carries no stock
    (30 -- (ops-search start-state-30 '((orientation stock-1 east-west)) ops :world world-30) => nil)
    ;deposit-stock-to-exchange when exchange already has stock
    (32 -- (ops-search start-state-32 '((at stock-1 exc-1)) ops :world world-32) => nil)
    ;move-from-exchange-to-corridor in wrong zone
    (33 -- (ops-search start-state-33 '((at car-1 corridor-1)) ops :world world-33) => nil)
    ))



(def small-world-1
  '#{(zone zone-1)
     (car car-1)
     (corridor cor-1)
     (junction jun-1)
     (bay bay-1)
     (bay bay-2)
     (stock stock-1)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (attaches jun-1 cor-1)
     (attaches cor-1 bay-1)
     (attaches cor-1 bay-2)
     (orientation cor-1 north-south)
     })

(def small-world-1-start-1
  '#{(at car-1 cor-1)
     (at stock-1 bay-1)
     (carries car-1 nil)
     (orientation stock-1 north-south)
     })

(def small-world-1-start-2
  '#{(at car-1 jun-1)
     (at stock-1 bay-1)
     (carries car-1 nil)
     (orientation stock-1 north-south)
     })

(def small-world-1-start-3
  '#{(at car-1 bay-1)
     (at stock-1 bay-2)
     (carries car-1 nil)
     (orientation stock-1 north-south)
     })

(def small-world-1-tests
  ;move from cor-1 to collect stock-1 from bay-1 and deposit at bay-2
  '( (34 -- (ops-search small-world-1-start-1 '((at stock-1 bay-2)) ops :world small-world-1) =>
       {:state #{(orientation stock-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)},
        ;start state
        :path (#{(at stock-1 bay-1) (at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 nil)}
               ;car-1 moves to bay-1
               #{(at stock-1 bay-1) (at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 nil)}
               ;car-1 collects stock-1
               #{(at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
               ;car-1 moves to cor-1
               #{(at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
               ;car-1 moves to bay-2
               #{(at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 stock-1)}),
        :cmds ([] [] [] [] []),
        :txt ((move car-1 from cor-1 to bay-1)
              (collect stock-1 from bay-1 to car-1)
              (move car-1 from bay-1 to cor-1)
              (move car-1 from cor-1 to bay-2)
              (deposit stock-1 from car-1 to bay-2))}
       )
    ;move from jun-1 to cor-1 to collect stock-1 from bay-1 and deposit at bay-2
    (35 -- (ops-search small-world-1-start-2 '((at stock-1 bay-2)) ops :world small-world-1) =>
      {:state #{(orientation stock-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)},
       ;start state
       :path (#{(at stock-1 bay-1) (at car-1 jun-1) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 moves to cor-1
              #{(at stock-1 bay-1) (at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 moves to bay-1
              #{(at stock-1 bay-1) (at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 collects stock-1
              #{(at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to cor-1
              #{(at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to bay-2
              #{(at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 stock-1)}),
       :cmds ([] [] [] [] [] []),
       :txt ((move car-1 from jun-1 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to bay-2)
             (deposit stock-1 from car-1 to bay-2))}
      )
    ;move from bay-1 to cor-1 to bay-2, collect stock-1 from bay-2 and deposit at bay-1
    (36 -- (ops-search small-world-1-start-3 '((at stock-1 bay-1)) ops :world small-world-1) =>
      {:state #{(orientation stock-1 north-south) (at car-1 bay-1) (carries car-1 nil) (at stock-1 bay-1)},
       ;start state
       :path (#{(at stock-1 bay-2) (at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 moves to cor-1
              #{(at stock-1 bay-2) (at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 moves to bay-2
              #{(at stock-1 bay-2) (at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 nil)}
              ;car-1 collects stock-1
              #{(at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to cor-1
              #{(at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to bay-1
              #{(at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 stock-1)}),
       :cmds ([] [] [] [] [] []),
       :txt ((move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to bay-2)
             (collect stock-1 from bay-2 to car-1)
             (move car-1 from bay-2 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (deposit stock-1 from car-1 to bay-1))}
      )
    ))


(def small-world-2
  '#{(zone zone-1)
     (car car-1)
     (corridor cor-1)
     (corridor cor-2)
     (junction jun-1)
     (bay bay-1)
     (bay bay-2)
     (stock stock-1)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (in cor-2 zone-1)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-2)
     (attaches cor-1 bay-1)
     (attaches cor-2 bay-2)
     (orientation cor-1 north-south)
     (orientation cor-2 east-west)
     })

(def small-world-2-start-1
  '#{(at car-1 jun-1)
     (at stock-1 bay-1)
     (carries car-1 nil)
     (orientation stock-1 north-south)
     })

(def small-world-2-tests
  ;move from jun-1 to bay-1 to collect stock-1, rotate stock-1 through jun-1 to deposit stock-1 at bay-3
  '(  (37 -- (ops-search small-world-2-start-1 '((at stock-1 bay-2)) ops :world small-world-2) =>
        {:state #{(at car-1 bay-2) (orientation stock-1 east-west) (carries car-1 nil) (at stock-1 bay-2)},
         ;start state
         :path (#{(at stock-1 bay-1) (at car-1 jun-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;move car-1 from jun-1 to cor-1
                #{(at stock-1 bay-1) (at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;move car-1 from cor-1 to bay-1
                #{(at stock-1 bay-1) (at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;car-1 picks up stock-1
                #{(at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 to cor-1
                #{(at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 to jun-1
                #{(at car-1 jun-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;rotate stock-1 from north-south to east-west
                #{(at car-1 jun-1) (orientation stock-1 east-west) (carries car-1 stock-1)}
                ;move car-1 from jun-1 to cor-2
                #{(at car-1 cor-2) (orientation stock-1 east-west) (carries car-1 stock-1)}
                ;move car-1 from cor-2 to bay-2
                #{(at car-1 bay-2) (orientation stock-1 east-west) (carries car-1 stock-1)}
                ),
         :cmds ([] [] [] [] [] [] [] [] []),
         :txt ((move car-1 from jun-1 to cor-1)
               (move car-1 from cor-1 to bay-1)
               (collect stock-1 from bay-1 to car-1)
               (move car-1 from bay-1 to cor-1)
               (move car-1 from cor-1 to jun-1)
               (rotate car-1 at jun-1 from north-south to east-west)
               (move car-1 from jun-1 to cor-2)
               (move car-1 from cor-2 to bay-2)
               (deposit stock-1 from car-1 to bay-2))}
        )
    ))

(def small-world-3
  '#{(zone zone-1)
     (car car-1)
     (corridor cor-1)
     (corridor cor-2)
     (junction jun-1)
     (bay bay-1)
     (bay bay-2)
     (stock stock-1)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (in cor-2 zone-1)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-2)
     (attaches cor-1 bay-1)
     (attaches cor-2 bay-2)
     (orientation cor-1 north-south)
     (orientation cor-2 north-south)
     })

(def small-world-3-start-1
  '#{(at car-1 jun-1)
     (at stock-1 bay-1)
     (carries car-1 nil)
     (orientation stock-1 north-south)
     })

(def small-world-3-tests
  ;move from jun-1 to bay-1 to collect stock-1, then move to bay-2 to deposit stock-1, no rotation required
  '(  (38 -- (ops-search small-world-3-start-1 '((at stock-1 bay-2)) ops :world small-world-3) =>
        {:state #{(at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 nil) (at stock-1 bay-2)},
         ;start state
         :path (#{(at stock-1 bay-1) (at car-1 jun-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;move car-1 from jun-1 to cor-1
                #{(at stock-1 bay-1) (at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;move car-1 from cor-1 to bay-1
                #{(at stock-1 bay-1) (at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 nil)}
                ;car-1 picks up stock-1
                #{(at car-1 bay-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 to cor-1
                #{(at car-1 cor-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 to jun-1
                #{(at car-1 jun-1) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 from jun-1 to cor-2
                #{(at car-1 cor-2) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ;move car-1 from cor-2 to bay-2
                #{(at car-1 bay-2) (orientation stock-1 north-south) (carries car-1 stock-1)}
                ),
         :cmds ([] [] [] [] [] [] [] []),
         :txt ((move car-1 from jun-1 to cor-1)
               (move car-1 from cor-1 to bay-1)
               (collect stock-1 from bay-1 to car-1)
               (move car-1 from bay-1 to cor-1)
               (move car-1 from cor-1 to jun-1)
               (move car-1 from jun-1 to cor-2)
               (move car-1 from cor-2 to bay-2)
               (deposit stock-1 from car-1 to bay-2))}
        )
    ))

(def small-world-4
  '#{(zone zone-1)
     (zone zone-2)
     (car car-1)
     (car car-2)
     (corridor cor-1)
     (corridor cor-2)
     (exchange exc-1)
     (bay bay-1)
     (bay bay-2)
     (stock stock-1)
     (stock stock-2)
     (stock stock-3)
     (stock stock-4)
     (stock stock-5)
     (stock stock-6)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (in car-2 zone-2)
     (in cor-2 zone-2)
     (attaches cor-1 exc-1)
     (attaches cor-2 exc-1)
     (attaches cor-1 bay-1)
     (attaches cor-2 bay-2)
     (orientation cor-1 north-south)
     (orientation cor-2 north-south)
     })

;only stock-1 at bay-1
(def small-world-4-start-1
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation stock-1 north-south)
     (orientation stock-2 north-south)
     (orientation stock-3 north-south)
     (orientation stock-4 north-south)
     (orientation stock-5 north-south)
     (orientation stock-6 north-south)
     })

;only stock-1 at bay-2
(def small-world-4-start-2
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-2)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation stock-1 north-south)
     (orientation stock-2 north-south)
     (orientation stock-3 north-south)
     (orientation stock-4 north-south)
     (orientation stock-5 north-south)
     (orientation stock-6 north-south)
     })

;three stock at each bay (1-3 bay-1, 4-6 bay-2)
(def small-world-4-start-3
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at stock-2 bay-1)
     (at stock-3 bay-1)
     (at stock-4 bay-2)
     (at stock-5 bay-2)
     (at stock-6 bay-2)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation stock-1 north-south)
     (orientation stock-2 north-south)
     (orientation stock-3 north-south)
     (orientation stock-4 north-south)
     (orientation stock-5 north-south)
     (orientation stock-6 north-south)
     })

(def small-world-4-tests
  ;car-1 move from cor-1 to bay-1 to collect stock-1, deposit at exc-1, car-2 collects and takes to bay-2
  '((39 -- (ops-search small-world-4-start-1 '((at stock-1 bay-2)) ops :world small-world-4) =>
      {:state #{(at car-2 bay-2) (orientation stock-1 north-south) (at car-1 exc-1) (carries car-1 nil)
                (carries car-2 nil) (at stock-1 bay-2) (at nil exc-1) (orientation stock-2 north-south)
                (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                (orientation stock-6 north-south)},
       ;start state
       :path  (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to exc-1
               #{(at car-2 exc-1) (at car-1 cor-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (at stock-1 bay-1) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to bay-1
               #{(at stock-1 bay-1) (at car-2 exc-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 picks up stock-1
               #{(at car-2 exc-1) (at car-1 bay-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to cor-1
               #{(at car-2 exc-1) (at car-1 cor-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 deposits stock-1 at exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (at stock-1 exc-1) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 picks up stock-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to cor-2
               #{(at car-2 cor-2) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to bay-2
               #{(at car-2 bay-2) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ),
       :cmds  ([] [] [] [] [] [] [] [] [] []),
       :txt   ((move car-2 from cor-2 to exc-1)
               (move car-1 from cor-1 to bay-1)
               (collect stock-1 from bay-1 to car-1)
               (move car-1 from bay-1 to cor-1)
               (move car-1 from cor-1 to exc-1)
               (deposit stock-1 from car-1 to exc-1)
               (collect stock-1 from exc-1 to car-2)
               (move car-2 from exc-1 to cor-2)
               (move car-2 from cor-2 to bay-2)
               (deposit stock-1 from car-2 to bay-2)
               )}
      )
    ;car-2 move from cor-2 to bay-2 to collect stock-1, deposit at exc-1, car-1 collects and takes to bay-1
    (40 -- (ops-search small-world-4-start-2 '((at stock-1 bay-1)) ops :world small-world-4) =>
      {:state #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south)
                (carries car-1 nil) (carries car-2 nil) (orientation stock-2 north-south)
                (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                (orientation stock-6 north-south)},
       ;start state
       :path  (#{(at car-2 cor-2) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-1 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to exc-1
               #{(at car-2 cor-2) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-1 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to bay-2
               #{(at car-2 bay-2) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-1 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 collects stock-1
               #{(carries car-2 stock-1) (at car-2 bay-2) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to cor-2
               #{(carries car-2 stock-1) (at car-2 cor-2) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to exc-1
               #{(at car-2 exc-1) (carries car-2 stock-1) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 deposits stock-1 at exc-1
               #{(at car-2 exc-1) (at stock-1 exc-1) (at car-1 exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 collects stock-1 from exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to cor-1
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to bay-1
               #{(at car-2 exc-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ),
       :cmds  ([] [] [] [] [] [] [] [] [] []),
       :txt   ((move car-1 from cor-1 to exc-1)
               (move car-2 from cor-2 to bay-2)
               (collect stock-1 from bay-2 to car-2)
               (move car-2 from bay-2 to cor-2)
               (move car-2 from cor-2 to exc-1)
               (deposit stock-1 from car-2 to exc-1)
               (collect stock-1 from exc-1 to car-1)
               (move car-1 from exc-1 to cor-1)
               (move car-1 from cor-1 to bay-1)
               (deposit stock-1 from car-1 to bay-1)
               )}
      )
    ;identical to test 39 but more stock at each bay
    (41 -- (ops-search small-world-4-start-3 '((at stock-1 bay-2)) ops :world small-world-4) =>
      {:state #{(at car-2 bay-2) (orientation stock-1 north-south) (at car-1 exc-1) (carries car-1 nil)
                (carries car-2 nil) (at stock-1 bay-2) (at nil exc-1) (at stock-2 bay-1) (at stock-3 bay-1)
                (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                (orientation stock-6 north-south)},
       ;start state
       :path  (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to exc-1
               #{(at car-2 exc-1) (at car-1 cor-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (at stock-1 bay-1) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to bay-1
               #{(at stock-1 bay-1) (at car-2 exc-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 picks up stock-1
               #{(at car-2 exc-1) (at car-1 bay-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to cor-1
               #{(at car-2 exc-1) (at car-1 cor-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (at nil exc-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 deposits stock-1 at exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (at stock-1 exc-1) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 picks up stock-1
               #{(at car-2 exc-1) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to cor-2
               #{(at car-2 cor-2) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to bay-2
               #{(at car-2 bay-2) (at car-1 exc-1) (orientation stock-1 north-south) (carries car-2 stock-1)
                 (at nil exc-1) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-4 bay-2) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ),
       :cmds  ([] [] [] [] [] [] [] [] [] []),
       :txt   ((move car-2 from cor-2 to exc-1)
               (move car-1 from cor-1 to bay-1)
               (collect stock-1 from bay-1 to car-1)
               (move car-1 from bay-1 to cor-1)
               (move car-1 from cor-1 to exc-1)
               (deposit stock-1 from car-1 to exc-1)
               (collect stock-1 from exc-1 to car-2)
               (move car-2 from exc-1 to cor-2)
               (move car-2 from cor-2 to bay-2)
               (deposit stock-1 from car-2 to bay-2)
               )}
      )
    ;identical to test 40 but more stock at each bay
    (42 -- (ops-search small-world-4-start-3 '((at stock-4 bay-1)) ops :world small-world-4) =>
      {:state #{(at car-2 exc-1) (at stock-4 bay-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south)
                (carries car-1 nil) (carries car-2 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                (orientation stock-6 north-south)},
       ;start state
       :path  (#{(at car-2 cor-2) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-4 bay-2) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to exc-1
               #{(at car-2 cor-2) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-4 bay-2) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to bay-2
               #{(at car-2 bay-2) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-1 nil)
                 (carries car-2 nil) (at stock-4 bay-2) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 collects stock-1
               #{(carries car-2 stock-4) (at car-2 bay-2) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to cor-2
               #{(carries car-2 stock-4) (at car-2 cor-2) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 moves to exc-1
               #{(at car-2 exc-1) (carries car-2 stock-4) (at car-1 exc-1) (at nil exc-1)
                 (orientation stock-1 north-south) (carries car-1 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-2 deposits stock-1 at exc-1
               #{(at car-2 exc-1) (at stock-4 exc-1) (at car-1 exc-1) (orientation stock-1 north-south)
                 (carries car-1 nil) (carries car-2 nil) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 collects stock-1 from exc-1
               #{(at car-2 exc-1) (at car-1 exc-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-4) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to cor-1
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-4) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ;car-1 moves to bay-1
               #{(at car-2 exc-1) (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south) (carries car-2 nil)
                 (carries car-1 stock-4) (at stock-2 bay-1) (at stock-3 bay-1)
                 (at stock-1 bay-1) (at stock-5 bay-2) (at stock-6 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               ),
       :cmds  ([] [] [] [] [] [] [] [] [] []),
       :txt   ((move car-1 from cor-1 to exc-1)
               (move car-2 from cor-2 to bay-2)
               (collect stock-4 from bay-2 to car-2)
               (move car-2 from bay-2 to cor-2)
               (move car-2 from cor-2 to exc-1)
               (deposit stock-4 from car-2 to exc-1)
               (collect stock-4 from exc-1 to car-1)
               (move car-1 from exc-1 to cor-1)
               (move car-1 from cor-1 to bay-1)
               (deposit stock-4 from car-1 to bay-1)
               )}
      )
    ;move stock-1 to bay-2 and stock-4 to bay-1
    (43 -- (ops-search small-world-4-start-3 '((at stock-1 bay-2) (at stock-4 bay-1)) ops :world small-world-4) =>
      {:state #{(at car-2 bay-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-4 bay-1)
                (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 nil) (at stock-1 bay-2) (at stock-5 bay-2) (orientation stock-2 north-south)
                (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                (orientation stock-6 north-south)},
       ;start state
       :path  (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 stock-4) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 stock-4) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 stock-4) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at stock-4 exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 stock-4)
                 (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 stock-4)
                 (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 stock-4)
                 (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-4 bay-1)
                 (at car-1 bay-1) (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1)
                 (carries car-1 nil) (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-4 bay-1) (at car-1 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-4 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-4 bay-1)
                 (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at stock-4 bay-1) (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at stock-4 bay-1) (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1)
                 (carries car-1 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at stock-4 bay-1) (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1)
                 (carries car-1 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}
               #{(carries car-2 stock-1) (at car-2 bay-2) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (at stock-4 bay-1) (at nil exc-1) (orientation stock-1 north-south) (at stock-2 bay-1)
                 (carries car-1 nil) (at stock-5 bay-2) (orientation stock-2 north-south)
                 (orientation stock-3 north-south) (orientation stock-4 north-south) (orientation stock-5 north-south)
                 (orientation stock-6 north-south)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt (
             (move car-1 from cor-1 to exc-1)
             (move car-2 from cor-2 to bay-2)
             (collect stock-4 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (deposit stock-4 from car-2 to exc-1)
             (collect stock-4 from exc-1 to car-1)
             (move car-1 from exc-1 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (deposit stock-4 from car-1 to bay-1)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to exc-1)
             (deposit stock-1 from car-1 to exc-1)
             (collect stock-1 from exc-1 to car-2)
             (move car-2 from exc-1 to cor-2)
             (move car-2 from cor-2 to bay-2)
             (deposit stock-1 from car-2 to bay-2)
             )}
      )
    ))

(def small-world-5
  '#{(zone zone-1)
     (zone zone-2)
     (car car-1)
     (car car-2)
     (corridor cor-1)
     (corridor cor-2)
     (corridor cor-3)
     (corridor cor-4)
     (junction jun-1)
     (junction jun-2)
     (exchange exc-1)
     (bay bay-1)
     (bay bay-2)
     (stock stock-1)
     (stock stock-2)
     (stock stock-3)
     (stock stock-4)
     (stock stock-5)
     (stock stock-6)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (in car-2 zone-2)
     (in cor-2 zone-2)
     (attaches cor-1 exc-1)
     (attaches cor-2 exc-1)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-3)
     (attaches jun-2 cor-2)
     (attaches jun-2 cor-4)
     (attaches cor-3 bay-1)
     (attaches cor-4 bay-2)
     (orientation cor-1 north-south)
     (orientation cor-2 north-south)
     (orientation cor-3 east-west)
     (orientation cor-4 east-west)
     })

(def small-world-5-start-1
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at stock-2 bay-1)
     (at stock-3 bay-1)
     (at stock-4 bay-2)
     (at stock-5 bay-2)
     (at stock-6 bay-2)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation stock-1 east-west)
     (orientation stock-2 east-west)
     (orientation stock-3 east-west)
     (orientation stock-4 east-west)
     (orientation stock-5 east-west)
     (orientation stock-6 east-west)
     })

(def small-world-5-tests-1
  ;collect stock-1 from bay-1 and deposit at bay-2
  '( (44 -- (ops-search small-world-5-start-1 '((at stock-1 bay-2)) ops :world small-world-5) =>
       {:state #{(orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (at stock-1 bay-2) (orientation stock-3 east-west) (at stock-5 bay-2)
                 (at stock-4 bay-2) (orientation stock-5 east-west)},
        ;start state
        :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west)
                 (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 cor-3)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 cor-3)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-2 nil)
                 (carries car-1 stock-1) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west)
                 (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (carries car-2 nil) (carries car-1 stock-1)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (orientation stock-2 east-west) (at stock-6 bay-2)
                 (at stock-3 bay-1) (orientation stock-4 east-west) (at nil exc-1) (at car-1 jun-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil) (carries car-1 stock-1)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at car-1 cor-1) (orientation stock-6 east-west) (orientation stock-2 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west) (at nil exc-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil) (carries car-1 stock-1)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (orientation stock-2 east-west) (at stock-6 bay-2)
                 (at stock-3 bay-1) (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-2 nil) (carries car-1 stock-1)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (orientation stock-6 east-west) (at stock-1 exc-1) (orientation stock-2 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (orientation stock-4 east-west)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (orientation stock-6 east-west) (orientation stock-2 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil) (orientation stock-3 east-west)
                 (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (orientation stock-6 east-west) (orientation stock-2 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil) (orientation stock-3 east-west)
                 (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-2 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1)
                 (orientation stock-1 north-south) (at stock-2 bay-1) (carries car-1 nil) (orientation stock-3 east-west)
                 (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-1) (orientation stock-6 east-west) (at car-2 cor-4) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-1) (orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-2 from cor-2 to exc-1)
              (move car-1 from cor-1 to jun-1)
              (move car-1 from jun-1 to cor-3)
              (move car-1 from cor-3 to bay-1)
              (collect stock-1 from bay-1 to car-1)
              (move car-1 from bay-1 to cor-3)
              (move car-1 from cor-3 to jun-1)
              (rotate car-1 at jun-1 from east-west to north-south)
              (move car-1 from jun-1 to cor-1)
              (move car-1 from cor-1 to exc-1)
              (deposit stock-1 from car-1 to exc-1)
              (collect stock-1 from exc-1 to car-2)
              (move car-2 from exc-1 to cor-2)
              (move car-2 from cor-2 to jun-2)
              (rotate car-2 at jun-2 from north-south to east-west)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (deposit stock-1 from car-2 to bay-2))}
       )
    ;collect stock-4 from bay-2 and deposit at bay-1
    (45 -- (ops-search small-world-5-start-1 '((at stock-4 bay-1)) ops :world small-world-5) =>
      {:state #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-4 bay-1) (at car-1 bay-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)},
       ;start state
       :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (orientation stock-6 east-west)
                (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (at car-2 cor-2) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 cor-4) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2)
                (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 stock-4) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 cor-4) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 stock-4) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 stock-4) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at car-1 exc-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 stock-4)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at stock-1 bay-1) (at car-2 cor-2) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at car-1 exc-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 stock-4)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at car-1 exc-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 stock-4)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at car-1 exc-1) (at stock-4 exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at car-1 exc-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-4) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (orientation stock-6 east-west)
                (orientation stock-1 east-west) (orientation stock-4 north-south) (orientation stock-2 east-west)
                (at stock-6 bay-2) (at stock-3 bay-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-4)
                (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-4 north-south) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (carries car-1 stock-4) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west)
                (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (carries car-1 stock-4) (carries car-2 nil)
                (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 cor-3)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-4)
                (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 bay-1)
                (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-4)
                (carries car-2 nil) (orientation stock-3 east-west) (at stock-5 bay-2) (orientation stock-5 east-west)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt ((move car-1 from cor-1 to exc-1)
             (move car-2 from cor-2 to jun-2)
             (move car-2 from jun-2 to cor-4)
             (move car-2 from cor-4 to bay-2)
             (collect stock-4 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-4)
             (move car-2 from cor-4 to jun-2)
             (rotate car-2 at jun-2 from east-west to north-south)
             (move car-2 from jun-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (deposit stock-4 from car-2 to exc-1)
             (collect stock-4 from exc-1 to car-1)
             (move car-1 from exc-1 to cor-1)
             (move car-1 from cor-1 to jun-1)
             (rotate car-1 at jun-1 from north-south to east-west)
             (move car-1 from jun-1 to cor-3)
             (move car-1 from cor-3 to bay-1)
             (deposit stock-4 from car-1 to bay-1))}
      )
    ))

(def small-world-5-tests-2
  ;collect stock-5 from bay-2 and deposit at bay-1, collect stock-2 for bay-1 and deposit at bay-2
  '( (46 -- (ops-search small-world-5-start-1 '((at stock-5 bay-1) (at stock-2 bay-2)) ops :world small-world-5) =>
       {:state #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2) (at stock-2 bay-2)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)},
        ;start state
        :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 cor-4) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-5 bay-2) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-5) (at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-5) (at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 cor-4)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-5) (at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(carries car-2 stock-5) (at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1)
                 (orientation stock-5 north-south) (carries car-1 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(carries car-2 stock-5) (at stock-1 bay-1) (at car-2 cor-2) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1)
                 (orientation stock-5 north-south) (carries car-1 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (carries car-2 stock-5) (at stock-1 bay-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1)
                 (at car-1 exc-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1)
                 (orientation stock-5 north-south) (carries car-1 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-5 exc-1)
                 (orientation stock-4 east-west) (at stock-2 bay-1) (orientation stock-5 north-south) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (orientation stock-5 north-south)
                 (carries car-1 stock-5) (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (orientation stock-2 east-west) (at stock-6 bay-2)
                 (at stock-3 bay-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1)
                 (orientation stock-5 north-south) (carries car-1 stock-5) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west)
                 (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (orientation stock-5 north-south)
                 (carries car-1 stock-5) (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-4 east-west)
                 (at nil exc-1) (at car-1 jun-1) (at stock-2 bay-1) (carries car-1 stock-5) (carries car-2 nil)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 cor-3)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-5)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 stock-5)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-5 bay-1)
                 (at car-1 bay-1) (orientation stock-4 east-west) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-5 bay-1)
                 (at car-1 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-5 bay-1)
                 (at car-1 cor-3) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at stock-5 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at car-1 jun-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at stock-5 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (at car-1 jun-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (orientation stock-6 east-west)
                 (orientation stock-1 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at car-1 exc-1)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 stock-2)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at car-1 exc-1)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (carries car-1 nil) (at stock-2 exc-1)
                 (carries car-2 nil) (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at car-1 exc-1)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil)
                 (carries car-2 stock-2) (orientation stock-3 east-west) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (orientation stock-6 east-west) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at car-1 exc-1)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil)
                 (carries car-2 stock-2) (orientation stock-3 east-west) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                 (at stock-6 bay-2) (at stock-3 bay-1) (orientation stock-2 north-south) (at car-1 exc-1)
                 (at stock-5 bay-1) (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil)
                 (carries car-2 stock-2) (orientation stock-3 east-west) (at stock-4 bay-2)
                 (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 jun-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-5 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil) (carries car-2 stock-2)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 cor-4) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-5 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil) (carries car-2 stock-2)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}
               #{(at stock-1 bay-1) (orientation stock-6 east-west) (at car-2 bay-2) (orientation stock-1 east-west)
                 (orientation stock-2 east-west) (at stock-6 bay-2) (at stock-3 bay-1) (at car-1 exc-1) (at stock-5 bay-1)
                 (orientation stock-4 east-west) (at nil exc-1) (carries car-1 nil) (carries car-2 stock-2)
                 (orientation stock-3 east-west) (at stock-4 bay-2) (orientation stock-5 east-west)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-1 from cor-1 to exc-1)
              (move car-2 from cor-2 to jun-2)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (collect stock-5 from bay-2 to car-2)
              (move car-2 from bay-2 to cor-4)
              (move car-2 from cor-4 to jun-2)
              (rotate car-2 at jun-2 from east-west to north-south)
              (move car-2 from jun-2 to cor-2)
              (move car-2 from cor-2 to exc-1)
              (deposit stock-5 from car-2 to exc-1)
              (collect stock-5 from exc-1 to car-1)
              (move car-1 from exc-1 to cor-1)
              (move car-1 from cor-1 to jun-1)
              (rotate car-1 at jun-1 from north-south to east-west)
              (move car-1 from jun-1 to cor-3)
              (move car-1 from cor-3 to bay-1)
              (deposit stock-5 from car-1 to bay-1)
              (collect stock-2 from bay-1 to car-1)
              (move car-1 from bay-1 to cor-3)
              (move car-1 from cor-3 to jun-1)
              (rotate car-1 at jun-1 from east-west to north-south)
              (move car-1 from jun-1 to cor-1)
              (move car-1 from cor-1 to exc-1)
              (deposit stock-2 from car-1 to exc-1)
              (collect stock-2 from exc-1 to car-2)
              (move car-2 from exc-1 to cor-2)
              (move car-2 from cor-2 to jun-2)
              (rotate car-2 at jun-2 from north-south to east-west)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (deposit stock-2 from car-2 to bay-2))}
       )
    ))

(def standard-world-1
  '#{(zone zone-1)
     (zone zone-2)
     (zone zone-3)
     (zone zone-4)
     (zone zone-5)
     (car car-1)
     (car car-2)
     (car car-3)
     (car car-4)
     (car car-5)
     (in car-1 zone-1)
     (in car-2 zone-2)
     (in car-3 zone-3)
     (in car-4 zone-4)
     (in car-5 zone-5)
     (corridor cor-1)
     (corridor cor-2)
     (corridor cor-3)
     (corridor cor-4)
     (corridor cor-5)
     (corridor cor-6)
     (corridor cor-7)
     (corridor cor-8)
     (corridor cor-9)
     (corridor cor-10)
     (corridor cor-11)
     (corridor cor-12)
     (corridor cor-13)
     (in cor-1 zone-2)
     (in cor-2 zone-2)
     (in cor-3 zone-1)
     (in cor-4 zone-1)
     (in cor-5 zone-1)
     (in cor-6 zone-1)
     (in cor-7 zone-3)
     (in cor-8 zone-3)
     (in cor-9 zone-3)
     (in cor-10 zone-2)
     (in cor-11 zone-5)
     (in cor-12 zone-3)
     (in cor-13 zone-4)
     (junction jun-1)
     (junction jun-2)
     (junction jun-3)
     (junction jun-4)
     (junction jun-5)
     (exchange exc-1)
     (junction jun-6)
     (exchange exc-3)
     (exchange exc-4)
     (exchange exc-5)
     (bay bay-1)
     (bay bay-2)
     (bay bay-3)
     (bay bay-4)
     (bay bay-5)
     (bay bay-6)
     (bay bay-7)
     (bay bay-8)
     (bay bay-9)
     (bay bay-10)
     (bay bay-11)
     (bay bay-12)
     (bay bay-13)
     (bay bay-14)
     (bay bay-15)
     (bay bay-16)
     (bay bay-17)
     (stock stock-1)
     ;(stock stock-2)
     ;(stock stock-3)
     ;(stock stock-4)
     ;(stock stock-5)
     ;(stock stock-6)
     ;(stock stock-7)
     ;(stock stock-8)
     ;(stock stock-9)
     ;(stock stock-10)
     ;(stock stock-11)
     ;(stock stock-12)
     ;(stock stock-13)
     ;(stock stock-14)
     (orientation cor-1 north-south)
     (orientation cor-2 east-west)
     (orientation cor-3 east-west)
     (orientation cor-4 north-south)
     (orientation cor-5 east-west)
     (orientation cor-6 north-south)
     (orientation cor-7 north-south)
     (orientation cor-8 north-south)
     (orientation cor-9 east-west)
     (orientation cor-10 north-south)
     (orientation cor-11 north-south)
     (orientation cor-12 east-west)
     (orientation cor-13 north-south)
     (attaches cor-1 bay-1)
     (attaches cor-1 bay-2)
     (attaches cor-2 bay-3)
     (attaches cor-2 bay-17)
     (attaches cor-2 exc-3)
     (attaches cor-2 exc-1)
     (attaches cor-3 exc-1)
     (attaches cor-3 bay-4)
     (attaches cor-4 bay-14)
     (attaches jun-6 cor-5)
     (attaches cor-5 bay-13)
     (attaches cor-6 exc-1)
     (attaches jun-6 cor-6)
     (attaches cor-6 bay-11)
     (attaches cor-6 bay-12)
     (attaches cor-7 exc-3)
     (attaches cor-7 bay-16)
     (attaches cor-9 bay-10)
     (attaches cor-10 exc-4)
     (attaches cor-10 bay-15)
     (attaches cor-11 exc-4)
     (attaches cor-11 bay-5)
     (attaches cor-11 bay-6)
     (attaches cor-12 exc-5)
     (attaches cor-12 bay-9)
     (attaches cor-13 exc-5)
     (attaches cor-13 bay-7)
     (attaches cor-13 bay-8)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-2)
     (attaches jun-1 cor-10)
     (attaches jun-2 cor-3)
     (attaches jun-2 cor-4)
     (attaches jun-2 cor-5)
     (attaches jun-3 cor-7)
     (attaches jun-3 cor-8)
     (attaches jun-4 cor-8)
     (attaches jun-4 cor-9)
     (attaches jun-5 cor-9)
     (attaches jun-5 cor-12)
     })

(def standard-world-1-start-1
  '#{                                                       ;(at car-1 jun-6)
     (at car-2 jun-1)
     (at car-3 jun-4)
     ;(at car-4 bay-5)
     ;(at car-5 bay-8)
     (at stock-1 bay-1)
     ;(at stock-2 bay-1)
     ;(at stock-3 bay-2)
     ;(at stock-4 bay-2)
     ;(at stock-5 bay-3)
     ;(at stock-6 bay-4)
     ;(at stock-7 bay-15)
     ;(at stock-8 bay-5)
     ;(at stock-9 bay-7)
     ;(at stock-10 bay-12)
     ;(at stock-11 bay-13)
     ;(at stock-12 bay-13)
     ;(at stock-13 bay-14)
     ;(at stock-14 bay-7)
     (at nil exc-1)
     (at nil exc-3)
     (at nil exc-4)
     ;(at nil exc-5)
     ;(carries car-1 nil)
     (carries car-2 nil)
     (carries car-3 nil)
     ;(carries car-4 nil)
     ;(carries car-5 nil)
     (orientation stock-1 north-south)
     ;(orientation stock-2 north-south)
     ;(orientation stock-3 east-west)
     ;(orientation stock-4 north-south)
     ;(orientation stock-5 east-west)
     ;(orientation stock-6 north-south)
     ;(orientation stock-7 east-west)
     ;(orientation stock-8 north-south)
     ;(orientation stock-9 east-west)
     ;(orientation stock-10 north-south)
     ;(orientation stock-11 east-west)
     ;(orientation stock-12 north-south)
     ;(orientation stock-13 east-west)
     ;(orientation stock-14 north-south)
     })

(def standard-world-1-start-2
  '#{(at car-2 jun-1)
     (at car-3 jun-4)
     (at car-5 bay-8)
     (at stock-1 bay-1)
     (at nil exc-3)
     (at nil exc-5)
     (carries car-2 nil)
     (carries car-3 nil)
     (carries car-5 nil)
     (orientation stock-1 north-south)
     })

(def standard-world-1-start-2
  '#{(at car-2 jun-1)
     (at car-3 jun-4)
     (at car-5 bay-8)
     (at stock-1 bay-1)
     (at nil exc-3)
     (at nil exc-5)
     (carries car-2 nil)
     (carries car-3 nil)
     (carries car-5 nil)
     (orientation stock-1 north-south)
     })

(def standard-world-1-start-3
  '#{(at car-5 bay-8)
     (at car-3 jun-4)
     (at stock-1 bay-7)
     (carries car-5 nil)
     (carries car-3 nil)
     (at nil exc-5)
     (orientation stock-1 north-south)
     })

(def standard-world-1-start-4
  '#{(at car-5 bay-7)
     (at stock-1 bay-7)
     (carries car-5 nil)
     (orientation stock-1 north-south)
     })

(def standard-world-1-test-1
  '( (46 -- (ops-search standard-world-1-start-1 '((at stock-1 exc-3)) ops :world standard-world-1) =>
       {:state #{(at car-2 exc-3) (at car-3 jun-4) (orientation stock-1 east-west) (at stock-1 exc-3) (at nil exc-1)
                 (at nil exc-4) (carries car-3 nil) (carries car-2 nil)},
        :path (#{(at stock-1 bay-1) (at car-2 jun-1) (at car-3 jun-4) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-3 jun-4) (at car-2 cor-1) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-3 jun-4) (at car-2 bay-1) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (carries car-2 nil)}
               #{(carries car-2 stock-1) (at car-3 jun-4) (at car-2 bay-1) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3)}
               #{(carries car-2 stock-1) (at car-3 jun-4) (at car-2 cor-1) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (at car-3 jun-4) (at nil exc-1) (at nil exc-4)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (at car-3 jun-4) (orientation stock-1 east-west)
                 (at nil exc-1) (at nil exc-4) (carries car-3 nil) (at nil exc-3)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at car-3 jun-4) (orientation stock-1 east-west)
                 (at nil exc-1) (at nil exc-4) (carries car-3 nil) (at nil exc-3)}
               #{(carries car-2 stock-1) (at car-2 exc-3) (at car-3 jun-4) (orientation stock-1 east-west)
                 (at nil exc-1) (at nil exc-4) (carries car-3 nil) (at nil exc-3)}),
        :cmds ([] [] [] [] [] [] [] [] []),
        :txt ((move car-2 from jun-1 to cor-1)
              (move car-2 from cor-1 to bay-1)
              (collect stock-1 from bay-1 to car-2)
              (move car-2 from bay-1 to cor-1)
              (move car-2 from cor-1 to jun-1)
              (rotate car-2 at jun-1 from north-south to east-west)
              (move car-2 from jun-1 to cor-2)
              (move car-2 from cor-2 to exc-3)
              (deposit stock-1 from car-2 to exc-3)
              )}
       )
    ))

(def standard-world-1-test-2
  '( (47 -- (ops-search standard-world-1-start-2 '((at stock-1 bay-16)) ops :world standard-world-1) =>
       {:state #{(at stock-1 bay-16) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at car-3 bay-16) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)},
        :path (#{(at stock-1 bay-1) (at car-2 jun-1) (at car-3 jun-4) (carries car-5 nil)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (at car-5 bay-8)
                 (carries car-2 nil) (at nil exc-5)}
               #{(at stock-1 bay-1) (at car-2 jun-1) (carries car-5 nil) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 cor-8) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 cor-8) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 jun-3) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (at car-3 cor-7)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (at car-5 bay-8)
                 (carries car-2 nil) (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)
                 (at car-3 exc-3)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 bay-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)
                 (at car-3 exc-3)}
               #{(carries car-2 stock-1) (carries car-5 nil) (at car-2 bay-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (carries car-5 nil) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil) (at stock-1 exc-3)
                 (carries car-3 nil) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-3 cor-7) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-3 bay-16) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               ),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-3 from jun-4 to cor-8)
              (move car-2 from jun-1 to cor-1)
              (move car-3 from cor-8 to jun-3)
              (move car-3 from jun-3 to cor-7)
              (move car-3 from cor-7 to exc-3)
              (move car-2 from cor-1 to bay-1)
              (collect stock-1 from bay-1 to car-2)
              (move car-2 from bay-1 to cor-1)
              (move car-2 from cor-1 to jun-1)
              (rotate car-2 at jun-1 from north-south to east-west)
              (move car-2 from jun-1 to cor-2)
              (move car-2 from cor-2 to exc-3)
              (deposit stock-1 from car-2 to exc-3)
              (collect stock-1 from exc-3 to car-3)
              (move car-3 from exc-3 to cor-7)
              (move car-3 from cor-7 to bay-16)
              (deposit stock-1 from car-3 to bay-16)
              )}
       )
    ))

(def standard-world-1-test-3
  '( (47 -- (ops-search standard-world-1-start-2 '((at stock-1 bay-10)) ops :world standard-world-1) =>
       {:state #{(at car-3 bay-10) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at stock-1 bay-10) (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)},
        :path (#{(at stock-1 bay-1) (at car-2 jun-1) (at car-3 jun-4) (carries car-5 nil)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (at car-5 bay-8)
                 (carries car-2 nil) (at nil exc-5)}
               #{(at stock-1 bay-1) (at car-2 jun-1) (carries car-5 nil) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 cor-8) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 cor-8) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 jun-3) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil)
                 (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (at car-3 cor-7)
                 (orientation stock-1 north-south) (carries car-3 nil) (at nil exc-3) (at car-5 bay-8)
                 (carries car-2 nil) (at nil exc-5)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)
                 (at car-3 exc-3)}
               #{(at stock-1 bay-1) (carries car-5 nil) (at car-2 bay-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)
                 (at car-3 exc-3)}
               #{(carries car-2 stock-1) (carries car-5 nil) (at car-2 bay-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (carries car-5 nil) (at car-2 cor-1) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (carries car-5 nil) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 jun-1) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-2 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (carries car-3 nil) (at nil exc-3) (at car-5 bay-8) (at nil exc-5) (at car-3 exc-3)}
               #{(at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil) (at stock-1 exc-3)
                 (carries car-3 nil) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5) (at car-3 exc-3)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-3 cor-7) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-3 jun-3) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (carries car-5 nil) (orientation stock-1 north-south)
                 (at car-3 jun-3) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (carries car-5 nil) (orientation stock-1 north-south)
                 (at car-3 cor-8) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (at car-3 jun-4) (carries car-5 nil)
                 (orientation stock-1 north-south) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (at car-3 jun-4) (orientation stock-1 east-west)
                 (carries car-5 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-3 cor-9) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}
               #{(at car-3 bay-10) (carries car-3 stock-1) (at car-2 exc-3) (orientation stock-1 east-west)
                 (carries car-5 nil) (at nil exc-3) (at car-5 bay-8) (carries car-2 nil) (at nil exc-5)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-3 from jun-4 to cor-8)
              (move car-2 from jun-1 to cor-1)
              (move car-3 from cor-8 to jun-3)
              (move car-3 from jun-3 to cor-7)
              (move car-3 from cor-7 to exc-3)
              (move car-2 from cor-1 to bay-1)
              (collect stock-1 from bay-1 to car-2)
              (move car-2 from bay-1 to cor-1)
              (move car-2 from cor-1 to jun-1)
              (rotate car-2 at jun-1 from north-south to east-west)
              (move car-2 from jun-1 to cor-2)
              (move car-2 from cor-2 to exc-3)
              (deposit stock-1 from car-2 to exc-3)
              (collect stock-1 from exc-3 to car-3)
              (move car-3 from exc-3 to cor-7)
              (move car-3 from cor-7 to jun-3)
              (rotate car-3 at jun-3 from east-west to north-south)
              (move car-3 from jun-3 to cor-8)
              (move car-3 from cor-8 to jun-4)
              (rotate car-3 at jun-4 from north-south to east-west)
              (move car-3 from jun-4 to cor-9)
              (move car-3 from cor-9 to bay-10)
              (deposit stock-1 from car-3 to bay-10)
              )}
       )
    ))

(def standard-world-1-test-4
  '( (47 -- (ops-search standard-world-1-start-3 '((at stock-1 bay-16)) ops :world standard-world-1) =>
       {:state #{(at stock-1 bay-16) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-3 bay-16) (at nil exc-5)},
        :path (#{(at car-3 jun-4) (at stock-1 bay-7) (carries car-5 nil) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-5 bay-8) (at nil exc-5)}
               #{(at stock-1 bay-7) (carries car-5 nil) (at car-3 cor-9) (orientation stock-1 north-south)
                 (carries car-3 nil) (at car-5 bay-8) (at nil exc-5)}
               #{(at stock-1 bay-7) (carries car-5 nil) (at car-3 cor-9) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-5) (at car-5 cor-13)}
               #{(at stock-1 bay-7) (carries car-5 nil) (orientation stock-1 north-south) (carries car-3 nil)
                 (at nil exc-5) (at car-3 jun-5) (at car-5 cor-13)}
               #{(at stock-1 bay-7) (carries car-5 nil) (at car-3 cor-12) (orientation stock-1 north-south)
                 (carries car-3 nil) (at nil exc-5) (at car-5 cor-13)}
               #{(at stock-1 bay-7) (carries car-5 nil) (orientation stock-1 north-south) (carries car-3 nil)
                 (at nil exc-5) (at car-3 exc-5) (at car-5 cor-13)}
               #{(at stock-1 bay-7) (carries car-5 nil) (orientation stock-1 north-south) (carries car-3 nil)
                 (at car-5 bay-7) (at nil exc-5) (at car-3 exc-5)}
               #{(orientation stock-1 north-south) (carries car-3 nil) (at car-5 bay-7) (carries car-5 stock-1)
                 (at nil exc-5) (at car-3 exc-5)}
               #{(orientation stock-1 north-south) (carries car-3 nil) (carries car-5 stock-1) (at nil exc-5)
                 (at car-3 exc-5) (at car-5 cor-13)}
               #{(at car-5 exc-5) (orientation stock-1 north-south) (carries car-3 nil) (carries car-5 stock-1)
                 (at nil exc-5) (at car-3 exc-5)}
               #{(carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south) (carries car-3 nil)
                 (at stock-1 exc-5) (at car-3 exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (at nil exc-5) (at car-3 exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (at car-3 cor-12)
                 (orientation stock-1 north-south) (at nil exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (at nil exc-5) (at car-3 jun-5)}
               #{(carries car-3 stock-1) (orientation stock-1 east-west) (carries car-5 nil) (at car-5 exc-5)
                 (at nil exc-5) (at car-3 jun-5)}
               #{(carries car-3 stock-1) (orientation stock-1 east-west) (carries car-5 nil) (at car-5 exc-5)
                 (at car-3 cor-9) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-3 jun-4) (orientation stock-1 east-west) (carries car-5 nil)
                 (at car-5 exc-5) (at nil exc-5)}
               #{(carries car-3 stock-1) (at car-3 jun-4) (carries car-5 nil) (at car-5 exc-5)
                 (orientation stock-1 north-south) (at nil exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (at car-3 cor-8) (at nil exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (at car-3 jun-3) (at nil exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (at car-3 cor-7)
                 (orientation stock-1 north-south) (at nil exc-5)}
               #{(carries car-3 stock-1) (carries car-5 nil) (at car-5 exc-5) (orientation stock-1 north-south)
                 (at car-3 bay-16) (at nil exc-5)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-3 from jun-4 to cor-9)
              (move car-5 from bay-8 to cor-13)
              (move car-3 from cor-9 to jun-5)
              (move car-3 from jun-5 to cor-12)
              (move car-3 from cor-12 to exc-5)
              (move car-5 from cor-13 to bay-7)
              (collect stock-1 from bay-7 to car-5)
              (move car-5 from bay-7 to cor-13)
              (move car-5 from cor-13 to exc-5)
              (deposit stock-1 from car-5 to exc-5)
              (collect stock-1 from exc-5 to car-3)
              (move car-3 from exc-5 to cor-12)
              (move car-3 from cor-12 to jun-5)
              (rotate car-3 at jun-5 from north-south to east-west)
              (move car-3 from jun-5 to cor-9)
              (move car-3 from cor-9 to jun-4)
              (rotate car-3 at jun-4 from east-west to north-south)
              (move car-3 from jun-4 to cor-8)
              (move car-3 from cor-8 to jun-3)
              (move car-3 from jun-3 to cor-7)
              (move car-3 from cor-7 to bay-16)
              (deposit stock-1 from car-3 to bay-16)
              )}
       )
    ))

(def standard-world-1-test-5
  '( (47 -- (ops-search standard-world-1-start-4 '((at stock-1 bay-8)) ops :world standard-world-1) =>
       {:state #{(carries car-5 nil) (orientation stock-1 north-south) (at stock-1 bay-8) (at car-5 bay-8)},
        :path (#{(at stock-1 bay-7) (carries car-5 nil) (orientation stock-1 north-south) (at car-5 bay-7)}
               #{(orientation stock-1 north-south) (at car-5 bay-7) (carries car-5 stock-1)}
               #{(orientation stock-1 north-south) (carries car-5 stock-1) (at car-5 cor-13)}
               #{(orientation stock-1 north-south) (carries car-5 stock-1) (at car-5 bay-8)}),
        :cmds ([] [] [] []),
        :txt ((collect stock-1 from bay-7 to car-5)
              (move car-5 from bay-7 to cor-13)
              (move car-5 from cor-13 to bay-8)
              (deposit stock-1 from car-5 to bay-8)
              )}
       )
    ))

(def standard-world-2
  '#{(zone zone-1)
     (zone zone-2)
     (zone zone-3)
     (car car-1)
     (car car-2)
     (car car-3)
     (in car-1 zone-1)
     (in car-2 zone-2)
     (in car-3 zone-3)
     (corridor cor-1)
     (corridor cor-2)
     (corridor cor-3)
     (corridor cor-4)
     (corridor cor-7)
     (corridor cor-8)
     (in cor-1 zone-2)
     (in cor-2 zone-2)
     (in cor-3 zone-1)
     (in cor-4 zone-1)
     (in cor-7 zone-3)
     (in cor-8 zone-3)
     (junction jun-1)
     (junction jun-2)
     (junction jun-3)
     (junction jun-4)
     (exchange exc-1)
     (exchange exc-3)
     (bay bay-1)
     (bay bay-3)
     (bay bay-10)
     (bay bay-14)
     (bay bay-16)
     (stock stock-1)
     (orientation cor-1 north-south)
     (orientation cor-2 east-west)
     (orientation cor-3 east-west)
     (orientation cor-4 north-south)
     (orientation cor-7 north-south)
     (orientation cor-8 north-south)
     (attaches cor-1 bay-1)
     (attaches cor-2 bay-3)
     (attaches cor-2 exc-3)
     (attaches cor-2 exc-1)
     (attaches cor-3 exc-1)
     (attaches cor-4 bay-14)
     (attaches cor-7 exc-3)
     (attaches cor-7 bay-16)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-2)
     (attaches jun-1 cor-10)
     (attaches jun-2 cor-3)
     (attaches jun-2 cor-4)
     (attaches jun-3 cor-7)
     (attaches jun-3 cor-8)
     (attaches jun-4 cor-8)
     (attaches jun-4 cor-9)
     })