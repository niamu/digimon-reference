# Digimon Reference

An index of every Digimon from the multilingual [Digimon Encyclopedia](https://digimon.net/reference/).

## Usage

```clj
user=> (require '[digimon-reference.core :as digimon-reference])

user=> (digimon-reference/book)

...
 #:reference{:profile "成長して二足歩行ができるようになった、小型の恐竜の様な姿をした爬虫類型デジモン。まだ成長途中なので力は弱いが、性格はかなり獰猛で恐いもの知らず。両手足には硬く鋭い爪が生えており、戦闘においても威力を発揮する。力ある偉大なデジモンへの進化を予測させる存在でもある。必殺技は口から火炎の息を吐き敵を攻撃する『ベビーフレイム』。",
             :name "アグモン",
             :type "爬虫類型",
             :id "https://digimon.net/reference/detail.php?directory_name=agumon",
             :image "https://digimon.net/cimages/digimon/agumon.jpg",
             :language "ja",
             :level "成長期",
             :new? false,
             :special-moves ["ベビーフレイム"],
             :attribute "ワクチン"}
 #:reference{:profile "A Reptile Digimon that’s developed the ability to walk on two legs, similar in appearance to a tiny dinosaur. Since Agumon isn’t fully matured, it lacks strength, but its fierce disposition makes it reckless. Hard, sharp claws grow from its limbs, and Agumon makes full use of them in battle. Agumon shows much potential, promising Digivolution into a great and powerful Digimon. Its special move is Pepper Breath, by which it spits flames at its foes.",
             :name "Agumon",
             :type "Reptile",
             :id "https://digimon.net/reference_en/detail.php?directory_name=agumon",
             :image "https://digimon.net/cimages/digimon/agumon.jpg",
             :language "en",
             :level "Rookie",
             :new? false,
             :special-moves ["Pepper Breath"],
             :attribute "Vaccine"}
 #:reference{:profile "成长为可以用双足步行，外形像小型恐龙的爬虫类型数码宝贝。还处于成长阶段，力量弱小，但性格却相当凶猛且无所畏惧。四肢长着坚硬锐利的爪子，在战斗中也能发挥威力。预测其可以进化为具有强大力量的数码宝贝。必杀技是从嘴中吐出火焰的气息攻击敌人的“小型火焰“。",
             :name "亚古兽",
             :type "爬虫类型",
             :id "https://digimon.net/reference_zh-CHS/detail.php?directory_name=agumon",
             :image "https://digimon.net/cimages/digimon/agumon.jpg",
             :language "zh-Hans",
             :level "成长期",
             :new? false,
             :special-moves ["小型火焰"],
             :attribute "疫苗种"}
 #:reference{:profile "성장해서 이족보행을 할 수 있게 된 소형 공룡과 같은 모습을 한 파충류형 디지몬. 아직 성장 중이기에 힘은 약하지만 성격은 꽤 사납고 두려움을 모른다. 양손 양발에는 단단하고 날카로운 손발톱이 나 있어 전투할 때도 위력을 발휘한다. 강한 힘을 가진 위대한 디지몬으로 진화하리라 예측되는 존재이기도 하다. 필살기는 입에서 화염의 숨결을 내뿜어 적을 공격하는 『꼬마 불꽃』이다.",
             :name "아구몬",
             :type "파충류형",
             :id "https://digimon.net/reference_ko/detail.php?directory_name=agumon",
             :image "https://digimon.net/cimages/digimon/agumon.jpg",
             :language "ko",
             :level "성장기",
             :new? false,
             :special-moves ["베이비 플레임"],
             :attribute "백신"}
...
```
