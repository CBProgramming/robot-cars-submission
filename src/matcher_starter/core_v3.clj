(ns matcher-starter.core-v3
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(defn tester [tests]                                        ;prints any test that fails, no output if all successful
  (doseq [test tests]
    (mif ['(?id -- ?pre => ?post) test]
         (when-not (= (eval (? pre)) (? post))
           (println 'Test 'Failed '- 'Id (? id) '-- 'Actual (eval(? pre))  '=> 'Expected  (? post))))))

(def ops-3
  '{collect-stock {:pre ((car ?car)
                         (stock ?stock)
                         (at ?car ?loc)
                         (at ?stock ?loc)
                         (carries ?car nil)
                         )
                   :add ((carries ?car ?stock)
                         (at nil ?loc))
                   :del  ((at ?stock ?loc)
                          (carries ?car nil))
                   :txt   (collect ?stock from ?loc to ?car)
                   :cmd []
                   }
    deposit-stock {:pre ((car ?car)
                         (stock ?stock)
                         (at nil ?loc)
                         (at ?car ?loc)
                         (carries ?car ?stock)
                         )
                   :add ((carries ?car nil)
                         (at ?stock ?loc))
                   :del  ((carries ?car ?stock)
                          (at nil ?loc))
                   :txt   (deposit ?stock from ?car to ?loc)
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
                                          (at ?car ?cor)
                                          (attaches ?junct ?cor)
                                          )
                                    :add ((at ?car ?junct))
                                    :del  ((at ?car ?cor))
                                    :txt   (move ?car from ?cor to ?junct)
                                    :cmd []
                                    }
    move-from-junction-to-corridor {:pre ((car ?car)
                                          (corridor ?cor)
                                          (junction ?junct)
                                          (attaches ?junct ?cor)
                                          (orientation ?car ?orientation)
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
    rotate-car {:pre ((car ?car)
                      (corridor ?cor)
                      (junction ?junct)
                      (attaches ?junct ?cor)
                      (at ?car ?junct)
                      (orientation ?car ?orientation-1)
                      (orientation ?cor ?orientation-2))
                :add ((orientation ?car ?orientation-2))
                :del  ((orientation ?car ?orientation-1))
                :txt   (rotate ?car at ?junct from ?orientation-1 to ?orientation-2)
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
  '#{(at nil bay-1)
     (at car-1 bay-1)
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
     (carries car-1 nil)
     (orientation corridor-1 north-west)})

(def start-state-8
  '#{(at car-1 junction-1)
     (orientation car-1 north-west)})

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
     (orientation car-1 north-south)})

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
     (orientation car-1 east-west)})

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
     (orientation car-1 north-south)})

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
  '#{(orientation car-1 east-west)})

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
  '#{(orientation car-1 north-south)})

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

(def ops-3-single-state-tests
  ;collect-stock-from-bay
  '( (1 -- (ops-search start-state-1 '((carries car-1 stock-1)) ops-3 :world world-1) =>
       {:state #{(at nil bay-1) (at car-1 bay-1) (carries car-1 stock-1)},
        :path (#{(at car-1 bay-1) (at stock-1 bay-1) (carries car-1 nil)}),
        :cmds ([]),
        :txt ((collect stock-1 from bay-1 to car-1))})
    ;collect-stock-from-exchange
    (2 -- (ops-search start-state-2 '((carries car-1 stock-1)) ops-3 :world world-2) =>
      {:state #{(at car-1 exc-1) (at nil exc-1) (carries car-1 stock-1)},
       :path (#{(at car-1 exc-1) (at stock-1 exc-1) (carries car-1 nil)}),
       :cmds ([]),
       :txt ((collect stock-1 from exc-1 to car-1))})
    ;deposit-stock-to-bay
    (3 -- (ops-search start-state-3 '((at stock-1 bay-1)) ops-3 :world world-3) =>
      {:state #{(at car-1 bay-1) (at stock-1 bay-1) (carries car-1 nil)},
       :path (#{(at nil bay-1) (at car-1 bay-1) (carries car-1 stock-1)}),
       :cmds ([]),
       :txt ((deposit stock-1 from car-1 to bay-1))})
    ;deposit-stock-to-exchange
    (4 -- (ops-search start-state-4 '((at stock-1 exc-1)) ops-3 :world world-4) =>
      {:state #{(at car-1 exc-1) (at stock-1 exc-1) (carries car-1 nil)},
       :path (#{(at car-1 exc-1) (at nil exc-1) (carries car-1 stock-1)}),
       :cmds ([]),
       :txt ((deposit stock-1 from car-1 to exc-1))})
    ;move-from-corridor-to-bay
    (5 -- (ops-search start-state-5 '((at car-1 bay-1)) ops-3 :world world-5) =>
      {:state #{(at car-1 bay-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to bay-1))})
    ;move-from-bay-to-corridor
    (6 -- (ops-search start-state-6 '((at car-1 corridor-1)) ops-3 :world world-6) =>
      {:state #{(at car-1 corridor-1)},
       :path (#{(at car-1 bay-1)}),
       :cmds ([]),
       :txt ((move car-1 from bay-1 to corridor-1))})
    ;move-from-corridor-to-junction
    (7 -- (ops-search start-state-7 '((at car-1 junction-1)) ops-3 :world world-7) =>
      {:state #{(at car-1 junction-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to junction-1))})
    ;move-from-junction-to-corridor-no-stock
    (8 -- (ops-search start-state-8 '((at car-1 corridor-1)) ops-3 :world world-8) =>
      {:state #{(at car-1 corridor-1) (orientation car-1 north-west)},
       :path (#{(at car-1 junction-1) (orientation car-1 north-west)}),
       :cmds ([]),
       :txt ((move car-1 from junction-1 to corridor-1))})
    ;move-from-junction-to-corridor-has-stock, stock is already aligned
    (9 -- (ops-search start-state-9 '((at car-1 corridor-1)) ops-3 :world world-9) =>
      {:state #{(at car-1 corridor-1) (orientation car-1 north-south)},
       :path (#{(at car-1 junction-1) (orientation car-1 north-south)}),
       :cmds ([]),
       :txt ((move car-1 from junction-1 to corridor-1))})
    ;move-from-junction-to-corridor-has-stock, stock is not aligned and currently east-west
    (10 -- (ops-search start-state-10 '((at car-1 corridor-1)) ops-3 :world world-10) =>
      {:state #{(at car-1 corridor-1)
                (orientation car-1 north-south)},
       :path (#{(at car-1 junction-1)(orientation car-1 east-west)}
              #{(at car-1 junction-1)(orientation car-1 north-south)}),
       :cmds ([] []),
       :txt ((rotate car-1 at junction-1 from east-west to north-south)(move car-1 from junction-1 to corridor-1)
             )})
    ;move-from-junction-to-corridor-has-stock, stock is not aligned and currently north-south
    (11 -- (ops-search start-state-11 '((at car-1 corridor-1)) ops-3 :world world-11) =>
      {:state #{(at car-1 corridor-1)
                (orientation car-1 east-west)},
       :path (#{(at car-1 junction-1)(orientation car-1 north-south)}
              #{(at car-1 junction-1)(orientation car-1 east-west)}),
       :cmds ([] []),
       :txt ((rotate car-1 at junction-1 from north-south to east-west)(move car-1 from junction-1 to corridor-1)
             )})
    ;move-from-corridor-to-exchange
    (12 -- (ops-search start-state-12 '((at car-1 exchange-1)) ops-3 :world world-12) =>
      {:state #{(at car-1 exchange-1)},
       :path (#{(at car-1 corridor-1)}),
       :cmds ([]),
       :txt ((move car-1 from corridor-1 to exchange-1)
             )})
    ;move-from-exchange-to-corridor
    (13 -- (ops-search start-state-13 '((at car-1 corridor-1)) ops-3 :world world-13) =>
      {:state #{(at car-1 corridor-1)},
       :path (#{(at car-1 exchange-1)}),
       :cmds ([]),
       :txt ((move car-1 from exchange-1 to corridor-1)
             )})
    ;rotate-car from east-west to north-south
    (14 -- (ops-search start-state-14 '((orientation car-1 north-south)) ops-3 :world world-14) =>
      {:state #{(orientation car-1 north-south)},
       :path (#{(orientation car-1 east-west)}),
       :cmds ([]),
       :txt ((rotate car-1 at junction-1 from east-west to north-south)
             )})
    ;rotate-car from north-south to east-west
    (15 -- (ops-search start-state-15 '((orientation car-1 east-west)) ops-3 :world world-15) =>
      {:state #{(orientation car-1 east-west)},
       :path (#{(orientation car-1 north-south)}),
       :cmds ([]),
       :txt ((rotate car-1 at junction-1 from north-south to east-west)
             )})
    ;deposit-stock-to-bay that already has stock
    (31 -- (ops-search start-state-31 '((at stock-1 bay-1)) ops-3 :world world-31) => nil)
    ;more from bay to corridor********************
    (48 -- (ops-search start-state-35 '((at car-1 corridor-1)) ops-3 :world world-35) =>
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
     (orientation car-1 north-south)})

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
     (orientation car-1 east-west)})

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
     (orientation car-1 north-south)})

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
  '#{(orientation car-1 east-west)})

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
  '#{(orientation car-1 north-south)})

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


(def illegal-operation-tests-ops-3
  ;collect-stock-from-bay with no stock
  '( (16 -- (ops-search start-state-16 '((carries car-1 stock-1)) ops-3 :world world-16) => nil)
    ;collect-stock-from-exchange with no stock
    (17 -- (ops-search start-state-17 '((carries car-1 stock-1)) ops-3 :world world-17) => nil)
    ;deposit-stock-to-bay when car has no stock
    (18 -- (ops-search start-state-18 '((at stock-1 bay-1)) ops-3 :world world-18) => nil)
    ;deposit-stock-to-exchange when car has no stock
    (19 -- (ops-search start-state-19 '((at stock-1 exc-1)) ops-3 :world world-19) => nil)
    ;move-from-corridor-to-bay when they are not connected
    (20 -- (ops-search start-state-20 '((at car-1 bay-1)) ops-3 :world world-20) => nil)
    ;move-from-bay-to-corridor when they are not connected
    (21 -- (ops-search start-state-21 '((at car-1 corridor-1)) ops-3 :world world-21) => nil)
    ;move-from-corridor-to-junction when they are not connected
    (22 -- (ops-search start-state-22 '((at car-1 junction-1)) ops-3 :world world-22) => nil)
    ;move-from-junction-to-corridor-no-stock  when they are not connected
    (23 -- (ops-search start-state-23 '((at car-1 corridor-1)) ops-3 :world world-23) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is already aligned
    (24 -- (ops-search start-state-24 '((at car-1 corridor-1)) ops-3 :world world-24) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is not aligned and currently east-west
    (25 -- (ops-search start-state-25 '((at car-1 corridor-1)) ops-3 :world world-25) => nil)
    ;move-from-junction-to-corridor-has-stock, when they are not connected, stock is not aligned and currently north-south
    (26 -- (ops-search start-state-26 '((at car-1 corridor-1)) ops-3 :world world-26) => nil)
    ;move-from-corridor-to-exchange when they are not connected
    (27 -- (ops-search start-state-27 '((at car-1 exchange-1)) ops-3 :world world-27) => nil)
    ;move-from-exchange-to-corridor when they are not connected
    (28 -- (ops-search start-state-28 '((at car-1 corridor-1)) ops-3 :world world-28) => nil)
    ;deposit-stock-to-exchange when exchange already has stock
    (29 -- (ops-search start-state-32 '((at stock-1 exc-1)) ops-3 :world world-32) => nil)
    ;move-from-exchange-to-corridor in wrong zone
    (30 -- (ops-search start-state-33 '((at car-1 corridor-1)) ops-3 :world world-33) => nil)
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

(def ops-3-small-world-1-start-1
  '#{(at car-1 cor-1)
     (at stock-1 bay-1)
     (at nil bay-2)
     (carries car-1 nil)
     (orientation car-1 north-south)
     })

(def small-world-1-start-2
  '#{(at car-1 jun-1)
     (at stock-1 bay-1)
     (at nil bay-2)
     (carries car-1 nil)
     (orientation car-1 north-south)
     })

(def small-world-1-start-3
  '#{(at car-1 bay-1)
     (at nil bay-1)
     (at stock-1 bay-2)
     (carries car-1 nil)
     (orientation car-1 north-south)
     })

(def ops-3-small-world-1-tests
  ;move from cor-1 to collect stock-1 from bay-1 and deposit at bay-2
  '( (34 -- (ops-search ops-3-small-world-1-start-1 '((at stock-1 bay-2)) ops-3 :world small-world-1) =>
       {:state #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)},
        ;start state
        :path (#{(at stock-1 bay-1) (at car-1 cor-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 nil)}
               ;car-1 moves to bay-1
               #{(at stock-1 bay-1) (at car-1 bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 nil)}
               ;car-1 collects stock-1
               #{(at nil bay-1) (at car-1 bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
               ;car-1 moves to cor-1
               #{(at car-1 cor-1) (at nil bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
               ;car-1 moves to bay-2
               #{(at nil bay-1) (at nil bay-2) (at car-1 bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}),
        :cmds ([] [] [] [] []),
        :txt ((move car-1 from cor-1 to bay-1)
              (collect stock-1 from bay-1 to car-1)
              (move car-1 from bay-1 to cor-1)
              (move car-1 from cor-1 to bay-2)
              (deposit stock-1 from car-1 to bay-2)
              )}
       )
    ;move from jun-1 to cor-1 to collect stock-1 from bay-1 and deposit at bay-2
    (35 -- (ops-search small-world-1-start-2 '((at stock-1 bay-2)) ops-3 :world small-world-1) =>
      {:state #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)},
       ;start state
       :path (#{(at stock-1 bay-1) (at nil bay-2) (at car-1 jun-1) (orientation car-1 north-south) (carries car-1 nil)}
              ;car-1 moves to cor-1
              #{(at stock-1 bay-1) (at car-1 cor-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 nil)}
              ;car-1 moves to bay-1
              #{(at stock-1 bay-1) (at car-1 bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 nil)}
              ;car-1 collects stock-1
              #{(at nil bay-1) (at car-1 bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to cor-1
              #{(at car-1 cor-1) (at nil bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to bay-2
              #{(at nil bay-1) (at nil bay-2) (orientation car-1 north-south) (at car-1 bay-2) (carries car-1 stock-1)}),
       :cmds ([] [] [] [] [] []),
       :txt ((move car-1 from jun-1 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to bay-2)
             (deposit stock-1 from car-1 to bay-2)
             )}
      )
    ;move from bay-1 to cor-1 to bay-2, collect stock-1 from bay-2 and deposit at bay-1
    (36 -- (ops-search small-world-1-start-3 '((at stock-1 bay-1)) ops-3 :world small-world-1) =>
      {:state #{(orientation car-1 north-south) (at car-1 bay-1) (at nil bay-2) (carries car-1 nil) (at stock-1 bay-1)},
       ;start state
       :path (#{(at nil bay-1) (at car-1 bay-1) (orientation car-1 north-south) (carries car-1 nil) (at stock-1 bay-2)}
              ;car-1 moves to cor-1
              #{(at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south) (carries car-1 nil) (at stock-1 bay-2)}
              ;car-1 moves to bay-2
              #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)}
              ;car-1 collects stock-1
              #{(at nil bay-1) (at nil bay-2) (at car-1 bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to cor-1
              #{(at car-1 cor-1) (at nil bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}
              ;car-1 moves to bay-1
              #{(at nil bay-1) (at car-1 bay-1) (at nil bay-2) (orientation car-1 north-south) (carries car-1 stock-1)}),
       :cmds ([] [] [] [] [] []),
       :txt ((move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to bay-2)
             (collect stock-1 from bay-2 to car-1)
             (move car-1 from bay-2 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (deposit stock-1 from car-1 to bay-1)
             )}
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
     (at nil bay-2)
     (carries car-1 nil)
     (orientation car-1 north-south)
     })

(def ops-3-small-world-2-tests
  ;move from jun-1 to bay-1 to collect stock-1, rotate car-1 through jun-1 to deposit stock-1 at bay-3
  '(  (37 -- (ops-search small-world-2-start-1 '((at stock-1 bay-2)) ops-3 :world small-world-2) =>
        {:state #{(at nil bay-1) (at car-1 bay-2) (carries car-1 nil) (orientation car-1 east-west) (at stock-1 bay-2)},
         ;start state
         :path (#{(at stock-1 bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 jun-1) (carries car-1 nil)}
                #{(at stock-1 bay-1) (at car-1 cor-1) (orientation car-1 north-south) (at nil bay-2) (carries car-1 nil)}
                #{(at stock-1 bay-1) (orientation car-1 north-south) (at car-1 bay-1) (at nil bay-2) (carries car-1 nil)}
                #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-1) (at nil bay-2) (carries car-1 stock-1)}
                #{(at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (carries car-1 stock-1)}
                #{(at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 jun-1) (carries car-1 stock-1)}
                #{(at nil bay-1) (at nil bay-2) (at car-1 jun-1) (orientation car-1 east-west) (carries car-1 stock-1)}
                #{(at nil bay-1) (at nil bay-2) (orientation car-1 east-west) (at car-1 cor-2) (carries car-1 stock-1)}
                #{(at nil bay-1) (at nil bay-2) (at car-1 bay-2) (orientation car-1 east-west) (carries car-1 stock-1)}
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
               (deposit stock-1 from car-1 to bay-2)
               )}
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
     (at nil bay-2)
     (carries car-1 nil)
     (orientation car-1 north-south)
     })

(def ops-3-small-world-3-tests
  ;move from jun-1 to bay-1 to collect stock-1, then move to bay-2 to deposit stock-1, no rotation required
  '(  (38 -- (ops-search small-world-3-start-1 '((at stock-1 bay-2)) ops-3 :world small-world-3) =>
        {:state #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-2) (carries car-1 nil) (at stock-1 bay-2)},
         ;start state
         :path (#{(at stock-1 bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 jun-1) (carries car-1 nil)}
                #{(at stock-1 bay-1) (at car-1 cor-1) (orientation car-1 north-south) (at nil bay-2) (carries car-1 nil)}
                #{(at stock-1 bay-1) (orientation car-1 north-south) (at car-1 bay-1) (at nil bay-2) (carries car-1 nil)}
                #{(at nil bay-1) (orientation car-1 north-south) (at car-1 bay-1) (at nil bay-2) (carries car-1 stock-1)}
                #{(at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (carries car-1 stock-1)}
                #{(at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 jun-1) (carries car-1 stock-1)}
                #{(at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 cor-2) (carries car-1 stock-1)}
                #{(at nil bay-1) (orientation car-1 north-south) (at nil bay-2) (at car-1 bay-2) (carries car-1 stock-1)}),
         :cmds ([] [] [] [] [] [] [] []),
         :txt ((move car-1 from jun-1 to cor-1)
               (move car-1 from cor-1 to bay-1)
               (collect stock-1 from bay-1 to car-1)
               (move car-1 from bay-1 to cor-1)
               (move car-1 from cor-1 to jun-1)
               (move car-1 from jun-1 to cor-2)
               (move car-1 from cor-2 to bay-2)
               (deposit stock-1 from car-1 to bay-2)
               )}
        )
    ))

(def small-world-4
  '#{(zone zone-1)
     (zone zone-2)
     (car car-1)
     (car car-2)
     (corridor cor-1)
     (corridor cor-2)
     (corridor cor-3)
     (exchange exc-1)
     (bay bay-1)
     (bay bay-2)
     (bay bay-3)
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
     (in cor-3 zone-2)
     (attaches cor-1 exc-1)
     (attaches cor-2 exc-1)
     (attaches cor-3 exc-1)
     (attaches cor-1 bay-1)
     (attaches cor-2 bay-2)
     (attaches cor-3 bay-3)
     (orientation cor-1 north-south)
     (orientation cor-2 north-south)
     (orientation cor-3 east-west)
     })

;only stock-1 at bay-1
(def small-world-4-start-1
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at nil bay-2)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation car-1 north-south)
     (orientation car-2 north-south)
     })

;only stock-1 at bay-2
(def small-world-4-start-2
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at nil bay-1)
     (at stock-1 bay-2)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation car-1 north-south)
     (orientation car-2 north-south)
     })

;one stock at two bays (bay-1, bay-2)
(def small-world-4-start-3
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at stock-2 bay-2)
     (at nil bay-3)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation car-1 north-south)
     (orientation car-2 north-south)
     })

(def ops-3-small-world-4-tests
  ;car-1 move from cor-1 to bay-1 to collect stock-1, deposit at exc-1, car-2 collects and takes to bay-2
  '((39 -- (ops-search small-world-4-start-1 '((at stock-1 bay-2)) ops-3 :world small-world-4) =>
      {:state #{(at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2)},
       ;start state
       :path  (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}
               #{(carries car-2 stock-1) (at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}),
       :cmds ([] [] [] [] [] [] [] [] [] []),
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
    (40 -- (ops-search small-world-4-start-2 '((at stock-1 bay-1)) ops-3 :world small-world-4) =>
      {:state #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)},
       ;start state
       :path  (#{(at car-2 cor-2) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2)}
               #{(at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2)}
               #{(at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2)}
               #{(carries car-2 stock-1) (at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)}),
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
    (41 -- (ops-search small-world-4-start-3 '((at stock-1 bay-2)) ops-3 :world small-world-4) =>
      {
       :state #{(at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2) (at stock-2 bay-3)},
       :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-2 bay-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-1 cor-1) (at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at stock-1 exc-1) (at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at stock-1 exc-1) (at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 cor-2) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (at car-2 cor-3) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (at car-2 bay-3)
                (carries car-2 stock-2)}
              #{(at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (at car-2 bay-3) (carries car-2 nil)
                (at stock-2 bay-3)}
              #{(at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at nil bay-2) (at car-2 cor-3) (carries car-1 nil) (carries car-2 nil)
                (at stock-2 bay-3)}
              #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)
                (at stock-2 bay-3)}
              #{(at car-2 exc-1) (carries car-2 stock-1) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (at stock-2 bay-3)}
              #{(carries car-2 stock-1) (at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (at stock-2 bay-3)}
              #{(carries car-2 stock-1) (at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (at stock-2 bay-3)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt ((move car-1 from cor-1 to bay-1)
             (move car-2 from cor-2 to bay-2)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to exc-1)
             (deposit stock-1 from car-1 to exc-1)
             (collect stock-2 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (move car-2 from exc-1 to cor-3)
             (move car-2 from cor-3 to bay-3)
             (deposit stock-2 from car-2 to bay-3)
             (move car-2 from bay-3 to cor-3)
             (move car-2 from cor-3 to exc-1)
             (collect stock-1 from exc-1 to car-2)
             (move car-2 from exc-1 to cor-2)
             (move car-2 from cor-2 to bay-2)
             (deposit stock-1 from car-2 to bay-2))}
      )
    ;identical to test 40 but more stock at each bay
    (42 -- (ops-search small-world-4-start-3 '((at stock-2 bay-1)) ops-3 :world small-world-4) =>
      {
       :state #{(at car-2 exc-1) (orientation car-1 north-south) (orientation car-2 north-south) (at stock-1 bay-3)
                (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil)
                (carries car-2 nil)},
       :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-2 bay-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at car-1 cor-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at stock-1 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1)
                (orientation car-1 north-south) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                (at nil exc-1) (carries car-1 nil)}
              #{(carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at car-2 cor-3) (carries car-1 nil)}
              #{(carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (at car-2 bay-3)}
              #{(at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at car-2 cor-3) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 cor-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil bay-2) (carries car-1 nil) (at stock-2 exc-1)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at stock-1 bay-3) (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2)
                (carries car-2 nil)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt ((move car-2 from cor-2 to exc-1)
             (move car-1 from cor-1 to bay-1)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to exc-1)
             (deposit stock-1 from car-1 to exc-1)
             (collect stock-1 from exc-1 to car-2)
             (move car-2 from exc-1 to cor-3)
             (move car-2 from cor-3 to bay-3)
             (deposit stock-1 from car-2 to bay-3)
             (move car-2 from bay-3 to cor-3)
             (move car-2 from cor-3 to exc-1)
             (move car-2 from exc-1 to cor-2)
             (move car-2 from cor-2 to bay-2)
             (collect stock-2 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (deposit stock-2 from car-2 to exc-1)
             (collect stock-2 from exc-1 to car-1)
             (move car-1 from exc-1 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (deposit stock-2 from car-1 to bay-1))})
    ;move stock-1 to bay-2 and stock-4 to bay-1
    (43 -- (ops-search small-world-4-start-3 '((at stock-1 bay-2) (at stock-2 bay-1)) ops-3 :world small-world-4) =>
      {
       :state #{(at car-2 bay-2) (orientation car-1 north-south) (at nil bay-3) (orientation car-2 north-south)
                (at car-1 bay-1) (at nil exc-1) (at stock-2 bay-1) (carries car-1 nil) (carries car-2 nil)
                (at stock-1 bay-2)},
       :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-1 bay-1) (at stock-2 bay-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at car-1 cor-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 exc-1) (at stock-1 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1) (carries car-1 nil) (carries car-2 nil)}
              #{(at car-2 exc-1) (carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1)
                (orientation car-1 north-south) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                (at nil exc-1) (carries car-1 nil)}
              #{(carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at car-2 cor-3) (carries car-1 nil)}
              #{(carries car-2 stock-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (at car-2 bay-3)}
              #{(at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at car-2 cor-3) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 cor-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil bay-2) (carries car-1 nil) (at stock-2 exc-1)
                (carries car-2 nil)}
              #{(at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at car-1 exc-1)
                (at stock-1 bay-3) (at nil bay-2) (at car-2 cor-3) (carries car-1 nil) (at stock-2 exc-1)
                (carries car-2 nil)}
              #{(at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at car-1 exc-1)
                (at stock-1 bay-3) (at nil bay-2) (carries car-1 nil) (at car-2 bay-3) (at stock-2 exc-1)
                (carries car-2 nil)}
              #{(at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at car-1 exc-1)
                (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south) (at stock-1 bay-3)
                (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(orientation car-1 north-south) (orientation car-2 north-south) (at stock-1 bay-3) (at car-1 bay-1)
                (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (at car-2 bay-3)
                (carries car-2 nil)}
              #{(carries car-2 stock-1) (orientation car-1 north-south) (at nil bay-3) (orientation car-2 north-south)
                (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (at car-2 bay-3)}
              #{(carries car-2 stock-1) (orientation car-1 north-south) (at nil bay-3) (orientation car-2 north-south)
                (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (at car-2 cor-3) (carries car-1 nil)}
              #{(at car-2 exc-1) (carries car-2 stock-1) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1)
                (carries car-1 nil)}
              #{(carries car-2 stock-1) (at car-2 cor-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1)
                (carries car-1 nil)}
              #{(carries car-2 stock-1) (at car-2 bay-2) (orientation car-1 north-south) (at nil bay-3)
                (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1)
                (carries car-1 nil)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt ((move car-2 from cor-2 to exc-1)
             (move car-1 from cor-1 to bay-1)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-1)
             (move car-1 from cor-1 to exc-1)
             (deposit stock-1 from car-1 to exc-1)
             (collect stock-1 from exc-1 to car-2)
             (move car-2 from exc-1 to cor-3)
             (move car-2 from cor-3 to bay-3)
             (deposit stock-1 from car-2 to bay-3)
             (move car-2 from bay-3 to cor-3)
             (move car-2 from cor-3 to exc-1)
             (move car-2 from exc-1 to cor-2)
             (move car-2 from cor-2 to bay-2)
             (collect stock-2 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (deposit stock-2 from car-2 to exc-1)
             (move car-2 from exc-1 to cor-3)
             (move car-2 from cor-3 to bay-3)
             (collect stock-2 from exc-1 to car-1)
             (move car-1 from exc-1 to cor-1)
             (move car-1 from cor-1 to bay-1)
             (deposit stock-2 from car-1 to bay-1)
             (collect stock-1 from bay-3 to car-2)
             (move car-2 from bay-3 to cor-3)
             (move car-2 from cor-3 to exc-1)
             (move car-2 from exc-1 to cor-2)
             (move car-2 from cor-2 to bay-2)
             (deposit stock-1 from car-2 to bay-2))})
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

(def small-world-5
  '#{(zone zone-1)
     (zone zone-2)
     (car car-1)
     (car car-2)
     (corridor cor-1)
     (corridor cor-2)
     (corridor cor-3)
     (corridor cor-4)
     (corridor cor-5)
     (junction jun-1)
     (junction jun-2)
     (exchange exc-1)
     (bay bay-1)
     (bay bay-2)
     (bay bay-3)
     (stock stock-1)
     (stock stock-2)
     (in car-1 zone-1)
     (in cor-1 zone-1)
     (in car-2 zone-2)
     (in cor-2 zone-2)
     (attaches cor-1 exc-1)
     (attaches cor-2 exc-1)
     (attaches jun-1 cor-1)
     (attaches jun-1 cor-3)
     (attaches jun-1 cor-5)
     (attaches jun-2 cor-2)
     (attaches jun-2 cor-4)
     (attaches cor-3 bay-1)
     (attaches cor-4 bay-2)
     (attaches cor-5 bay-3)
     (orientation cor-1 north-south)
     (orientation cor-2 north-south)
     (orientation cor-3 east-west)
     (orientation cor-4 east-west)
     (orientation cor-5 east-west)
     })

(def small-world-5-start-1
  '#{(at car-1 cor-1)
     (at car-2 cor-2)
     (at stock-1 bay-1)
     (at stock-2 bay-2)
     (at nil bay-3)
     (at nil exc-1)
     (carries car-1 nil)
     (carries car-2 nil)
     (orientation car-1 east-west)
     (orientation car-2 east-west)
     })

(def ops-3-small-world-5-tests-1
  ;collect stock-1 from bay-1 and deposit at bay-2
  '( (44 -- (ops-search small-world-5-start-1 '((at stock-1 bay-2)) ops-3 :world small-world-5) =>
       {
        :state #{(at car-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (orientation car-1 north-south)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (carries car-2 nil) (at stock-1 bay-2)
                 (at stock-2 bay-3)},
        :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-2 east-west)
                 (at nil bay-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at stock-2 bay-2) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (at stock-2 exc-1)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (at nil bay-3) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (at car-1 jun-1) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2) (carries car-2 nil)
                 (at car-1 cor-5)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 bay-3) (at nil bay-3) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil) (at car-1 cor-5)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at car-1 cor-3) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at car-1 cor-3) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (at car-1 jun-1) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (at car-1 jun-1) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}
               #{(carries car-2 stock-1) (at car-2 jun-2) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}
               #{(carries car-2 stock-1) (at car-2 jun-2) (orientation car-2 east-west) (at nil bay-1)
                 (orientation car-1 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}
               #{(carries car-2 stock-1) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-1)
                 (orientation car-1 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}
               #{(carries car-2 stock-1) (at car-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                 (orientation car-1 north-south) (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                 (at stock-2 bay-3)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-1 from cor-1 to exc-1)
              (move car-2 from cor-2 to jun-2)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (collect stock-2 from bay-2 to car-2)
              (move car-2 from bay-2 to cor-4)
              (move car-2 from cor-4 to jun-2)
              (rotate car-2 at jun-2 from east-west to north-south)
              (move car-2 from jun-2 to cor-2)
              (move car-2 from cor-2 to exc-1)
              (deposit stock-2 from car-2 to exc-1)
              (collect stock-2 from exc-1 to car-1)
              (move car-1 from exc-1 to cor-1)
              (move car-1 from cor-1 to jun-1)
              (move car-1 from jun-1 to cor-5)
              (move car-1 from cor-5 to bay-3)
              (deposit stock-2 from car-1 to bay-3)
              (move car-1 from bay-3 to cor-5)
              (move car-1 from cor-5 to jun-1)
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
              (deposit stock-1 from car-2 to bay-2))})
    ;collect stock-2 from bay-2 and deposit at bay-1
    (45 -- (ops-search small-world-5-start-1 '((at stock-2 bay-1)) ops-3 :world small-world-5) =>
      {
       :state #{(at car-2 exc-1) (orientation car-2 north-south) (at stock-1 bay-3) (at car-1 bay-1) (at nil exc-1)
                (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)},
       :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-2 east-west)
                (at nil bay-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                (at nil exc-1) (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 jun-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                (at nil exc-1) (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 jun-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                (at car-1 cor-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at stock-2 bay-2) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3)
                (at car-1 cor-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at stock-2 bay-2) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3)
                (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                (at car-1 bay-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at nil bay-3)
                (at car-1 bay-1) (at nil exc-1) (orientation car-1 east-west) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at nil bay-3)
                (at car-1 cor-3) (at nil exc-1) (orientation car-1 east-west) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at nil bay-3)
                (at nil exc-1) (at car-1 jun-1) (orientation car-1 east-west) (carries car-2 nil)
                (carries car-1 stock-1)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at nil bay-3)
                (at nil exc-1) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                (at car-1 cor-5)}
              #{(at car-1 bay-3) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                (at nil bay-3) (at nil exc-1) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)}
              #{(at car-1 bay-3) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at stock-1 bay-3)
                (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil) (at car-1 cor-5)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (at stock-1 bay-3)
                (at nil exc-1) (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                (orientation car-1 north-south) (at stock-1 bay-3) (at nil exc-1) (at car-1 jun-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-1 cor-1) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                (orientation car-1 north-south) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-1)
                (orientation car-1 north-south) (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (carries car-1 nil)
                (carries car-2 nil)}
              #{(at car-2 bay-2) (orientation car-2 east-west) (at nil bay-1) (orientation car-1 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 cor-4) (orientation car-2 east-west) (at nil bay-1) (orientation car-1 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 jun-2) (orientation car-2 east-west) (at nil bay-1) (orientation car-1 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 jun-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 cor-2) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 nil)
                (carries car-2 stock-2)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil bay-2) (carries car-1 nil) (at stock-2 exc-1)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at car-1 exc-1) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                (orientation car-2 north-south) (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                (at stock-1 bay-3) (at nil exc-1) (at nil bay-2) (at car-1 jun-1) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at stock-1 bay-3) (at nil exc-1)
                (at nil bay-2) (at car-1 jun-1) (orientation car-1 east-west) (carries car-1 stock-2)
                (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at stock-1 bay-3) (at car-1 cor-3)
                (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2) (carries car-2 nil)}
              #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at stock-1 bay-3) (at car-1 bay-1)
                (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                (carries car-2 nil)}),
       :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
       :txt ((move car-1 from cor-1 to jun-1)
             (move car-2 from cor-2 to jun-2)
             (move car-1 from jun-1 to cor-3)
             (move car-2 from jun-2 to cor-4)
             (move car-1 from cor-3 to bay-1)
             (move car-2 from cor-4 to bay-2)
             (collect stock-1 from bay-1 to car-1)
             (move car-1 from bay-1 to cor-3)
             (move car-1 from cor-3 to jun-1)
             (move car-1 from jun-1 to cor-5)
             (move car-1 from cor-5 to bay-3)
             (deposit stock-1 from car-1 to bay-3)
             (move car-1 from bay-3 to cor-5)
             (move car-1 from cor-5 to jun-1)
             (rotate car-1 at jun-1 from east-west to north-south)
             (move car-1 from jun-1 to cor-1)
             (move car-1 from cor-1 to exc-1)
             (collect stock-2 from bay-2 to car-2)
             (move car-2 from bay-2 to cor-4)
             (move car-2 from cor-4 to jun-2)
             (rotate car-2 at jun-2 from east-west to north-south)
             (move car-2 from jun-2 to cor-2)
             (move car-2 from cor-2 to exc-1)
             (deposit stock-2 from car-2 to exc-1)
             (collect stock-2 from exc-1 to car-1)
             (move car-1 from exc-1 to cor-1)
             (move car-1 from cor-1 to jun-1)
             (rotate car-1 at jun-1 from north-south to east-west)
             (move car-1 from jun-1 to cor-3)
             (move car-1 from cor-3 to bay-1)
             (deposit stock-2 from car-1 to bay-1)
             )}
      )))

(def ops-3-small-world-5-tests-2
  ;collect stock-2 from bay-2 and deposit at bay-1, collect stock-2 for bay-1 and deposit at bay-2
  '( (46 -- (ops-search small-world-5-start-1 '((at stock-2 bay-1) (at stock-1 bay-2)) ops-3 :world small-world-5) =>
       {
        :state #{(at car-2 bay-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 bay-1) (at nil exc-1)
                 (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-1 bay-2)},
        :path (#{(at stock-1 bay-1) (at car-2 cor-2) (at car-1 cor-1) (at stock-2 bay-2) (orientation car-2 east-west)
                 (at nil bay-3) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at stock-2 bay-2) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (at stock-2 bay-2) (orientation car-2 east-west) (at nil bay-3)
                 (at car-1 exc-1) (at nil exc-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)}
               #{(at stock-1 bay-1) (at car-2 bay-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 jun-2) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at stock-1 bay-1) (at car-2 cor-2) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 stock-2)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (at stock-2 exc-1)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 exc-1)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 cor-1) (at nil bay-3) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (at car-1 jun-1) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at nil bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2) (carries car-2 nil)
                 (at car-1 cor-5)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 bay-3) (at nil bay-3) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (at car-1 bay-3) (orientation car-2 north-south) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil) (at car-1 cor-5)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at car-1 cor-3) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 bay-1) (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at car-1 bay-1) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at car-1 cor-3) (at nil exc-1)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-2 north-south) (at nil exc-1) (at nil bay-2)
                 (at car-1 jun-1) (orientation car-1 east-west) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at nil exc-1) (at nil bay-2) (at car-1 jun-1) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil exc-1) (at nil bay-2) (carries car-2 nil)
                 (carries car-1 stock-1) (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at nil bay-1) (orientation car-1 north-south) (orientation car-2 north-south)
                 (at car-1 exc-1) (at nil exc-1) (at nil bay-2) (carries car-2 nil) (carries car-1 stock-1)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at car-1 exc-1) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at car-1 cor-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil bay-2) (carries car-1 nil) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-1 north-south)
                 (orientation car-2 north-south) (at nil bay-2) (at car-1 jun-1) (carries car-1 nil) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-2 north-south) (at nil bay-2)
                 (at car-1 jun-1) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (orientation car-2 north-south) (at nil bay-2)
                 (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil) (at car-1 cor-5)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at car-1 bay-3) (at stock-1 exc-1) (at nil bay-1) (orientation car-2 north-south)
                 (at nil bay-2) (carries car-1 nil) (orientation car-1 east-west) (carries car-2 nil)
                 (at stock-2 bay-3)}
               #{(at car-2 exc-1) (at car-1 bay-3) (at stock-1 exc-1) (at nil bay-1) (at nil bay-3)
                 (orientation car-2 north-south) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (at nil bay-3) (orientation car-2 north-south)
                 (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2) (carries car-2 nil)
                 (at car-1 cor-5)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (at nil bay-3) (orientation car-2 north-south)
                 (at nil bay-2) (at car-1 jun-1) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (at nil bay-3) (orientation car-2 north-south)
                 (at car-1 cor-3) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-1) (at nil bay-3) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil bay-2) (orientation car-1 east-west) (carries car-1 stock-2)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (at stock-1 exc-1) (at nil bay-3) (orientation car-2 north-south) (at car-1 bay-1)
                 (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west)
                 (carries car-2 nil)}
               #{(at car-2 exc-1) (carries car-2 stock-1) (at nil bay-3) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation car-1 east-west)}
               #{(carries car-2 stock-1) (at car-2 cor-2) (at nil bay-3) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation car-1 east-west)}
               #{(carries car-2 stock-1) (at car-2 jun-2) (at nil bay-3) (orientation car-2 north-south)
                 (at car-1 bay-1) (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil)
                 (orientation car-1 east-west)}
               #{(carries car-2 stock-1) (at car-2 jun-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 bay-1)
                 (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west)}
               #{(carries car-2 stock-1) (at car-2 cor-4) (orientation car-2 east-west) (at nil bay-3) (at car-1 bay-1)
                 (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west)}
               #{(carries car-2 stock-1) (at car-2 bay-2) (orientation car-2 east-west) (at nil bay-3) (at car-1 bay-1)
                 (at nil exc-1) (at nil bay-2) (at stock-2 bay-1) (carries car-1 nil) (orientation car-1 east-west)}),
        :cmds ([] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] []),
        :txt ((move car-1 from cor-1 to exc-1)
              (move car-2 from cor-2 to jun-2)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (collect stock-2 from bay-2 to car-2)
              (move car-2 from bay-2 to cor-4)
              (move car-2 from cor-4 to jun-2)
              (rotate car-2 at jun-2 from east-west to north-south)
              (move car-2 from jun-2 to cor-2)
              (move car-2 from cor-2 to exc-1)
              (deposit stock-2 from car-2 to exc-1)
              (collect stock-2 from exc-1 to car-1)
              (move car-1 from exc-1 to cor-1)
              (move car-1 from cor-1 to jun-1)
              (move car-1 from jun-1 to cor-5)
              (move car-1 from cor-5 to bay-3)
              (deposit stock-2 from car-1 to bay-3)
              (move car-1 from bay-3 to cor-5)
              (move car-1 from cor-5 to jun-1)
              (move car-1 from jun-1 to cor-3)
              (move car-1 from cor-3 to bay-1)
              (collect stock-1 from bay-1 to car-1)
              (move car-1 from bay-1 to cor-3)
              (move car-1 from cor-3 to jun-1)
              (rotate car-1 at jun-1 from east-west to north-south)
              (move car-1 from jun-1 to cor-1)
              (move car-1 from cor-1 to exc-1)
              (deposit stock-1 from car-1 to exc-1)
              (move car-1 from exc-1 to cor-1)
              (move car-1 from cor-1 to jun-1)
              (rotate car-1 at jun-1 from north-south to east-west)
              (move car-1 from jun-1 to cor-5)
              (move car-1 from cor-5 to bay-3)
              (collect stock-2 from bay-3 to car-1)
              (move car-1 from bay-3 to cor-5)
              (move car-1 from cor-5 to jun-1)
              (move car-1 from jun-1 to cor-3)
              (move car-1 from cor-3 to bay-1)
              (deposit stock-2 from car-1 to bay-1)
              (collect stock-1 from exc-1 to car-2)
              (move car-2 from exc-1 to cor-2)
              (move car-2 from cor-2 to jun-2)
              (rotate car-2 at jun-2 from north-south to east-west)
              (move car-2 from jun-2 to cor-4)
              (move car-2 from cor-4 to bay-2)
              (deposit stock-1 from car-2 to bay-2)
              )}
       )
    ))