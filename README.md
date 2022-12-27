# Digimon Reference

An index of every Digimon from the multilingual [Digimon Encyclopedia](https://digimon.net/reference/).

## Usage

```clj
user=> (require '[digimon-reference.core :as digimon-reference])

user=> (digimon-reference/book)

({:reference/id "ja/シリウスモン",
  :reference/name "シリウスモン",
  :reference/href "https://digimon.net/reference/detail.php?directory_name=siriusmon",
  :reference/image "https://digimon.net/cimages/digimon/siriusmon.jpg",
  :reference/lang :ja}
 {:reference/id "romaji/SIRIUSMON",
  :reference/name "SIRIUSMON",
  :reference/href "https://digimon.net/reference/detail.php?directory_name=siriusmon",
  :reference/image "https://digimon.net/cimages/digimon/siriusmon.jpg",
  :reference/lang :en}
 {:reference/id "en/シリウスモン",
  :reference/name "Siriusmon",
  :reference/href "https://digimon.net/reference/detail.php?directory_name=siriusmon",
  :reference/image "https://digimon.net/cimages/digimon/siriusmon.jpg",
  :reference/lang :en}
 {:reference/id "zh/シリウスモン",
  :reference/name "天狼星",
  :reference/href "https://digimon.net/reference/detail.php?directory_name=siriusmon",
  :reference/image "https://digimon.net/cimages/digimon/siriusmon.jpg",
  :reference/lang :zh}
 {:reference/id "ja/シャイングレイモン：ルインモード",
  :reference/name "シャイングレイモン：ルインモード",
  :reference/href "https://digimon.net/reference/detail.php?directory_name=shinegreymon_ruin",
  :reference/image "https://digimon.net/cimages/digimon/shinegreymon_ruin.jpg",
  :reference/lang :ja}
...
```