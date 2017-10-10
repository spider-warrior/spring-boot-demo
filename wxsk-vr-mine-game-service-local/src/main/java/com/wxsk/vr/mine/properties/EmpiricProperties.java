package com.wxsk.vr.mine.properties;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.springframework.stereotype.Component;

@Component
public class EmpiricProperties {

    /**
     * 体力兑换经验值系数
     */
    public int energyToEmpiricRate = 20;
    /**
     * 经验值对应等级的映射
     **/
    public final RangeMap<Integer, Integer> levelRangeMap = TreeRangeMap.create();

    public int getEnergyToEmpiricRate() {
        return energyToEmpiricRate;
    }

    public void setEnergyToEmpiricRate(int energyToEmpiricRate) {
        this.energyToEmpiricRate = energyToEmpiricRate;
    }

    public RangeMap<Integer, Integer> getLevelRangeMap() {
        return levelRangeMap;
    }

    {
        levelRangeMap.put(Range.closedOpen(0, 480), 1);
        levelRangeMap.put(Range.closedOpen(480, 960), 2);
        levelRangeMap.put(Range.closedOpen(960, 1440), 3);
        levelRangeMap.put(Range.closedOpen(1440, 1920), 4);
        levelRangeMap.put(Range.closedOpen(1920, 2400), 5);
        levelRangeMap.put(Range.closedOpen(2400, 2880), 6);
        levelRangeMap.put(Range.closedOpen(2880, 3360), 7);
        levelRangeMap.put(Range.closedOpen(3360, 3840), 8);
        levelRangeMap.put(Range.closedOpen(3840, 4320), 9);
        levelRangeMap.put(Range.closedOpen(4320, 4800), 10);
        levelRangeMap.put(Range.closedOpen(4800, 5780), 11);
        levelRangeMap.put(Range.closedOpen(5780, 6760), 12);
        levelRangeMap.put(Range.closedOpen(6760, 7740), 13);
        levelRangeMap.put(Range.closedOpen(7740, 8720), 14);
        levelRangeMap.put(Range.closedOpen(8720, 9700), 15);
        levelRangeMap.put(Range.closedOpen(9700, 10680), 16);
        levelRangeMap.put(Range.closedOpen(10680, 11660), 17);
        levelRangeMap.put(Range.closedOpen(11660, 12640), 18);
        levelRangeMap.put(Range.closedOpen(12640, 13620), 19);
        levelRangeMap.put(Range.closedOpen(13620, 14600), 20);
        levelRangeMap.put(Range.closedOpen(14600, 16060), 21);
        levelRangeMap.put(Range.closedOpen(16060, 18980), 22);
        levelRangeMap.put(Range.closedOpen(18980, 23380), 23);
        levelRangeMap.put(Range.closedOpen(23380, 29240), 24);
        levelRangeMap.put(Range.closedOpen(29240, 36560), 25);
        levelRangeMap.put(Range.closedOpen(36560, 45340), 26);
        levelRangeMap.put(Range.closedOpen(45340, 55580), 27);
        levelRangeMap.put(Range.closedOpen(55580, 67300), 28);
        levelRangeMap.put(Range.closedOpen(67300, 79980), 29);
        levelRangeMap.put(Range.closedOpen(79980, 93640), 30);
        levelRangeMap.put(Range.closedOpen(93640, 108140), 31);
        levelRangeMap.put(Range.closedOpen(108140, 124100), 32);
        levelRangeMap.put(Range.closedOpen(124100, 141580), 33);
        levelRangeMap.put(Range.closedOpen(141580, 160680), 34);
        levelRangeMap.put(Range.closedOpen(160680, 181460), 35);
        levelRangeMap.put(Range.closedOpen(181460, 204020), 36);
        levelRangeMap.put(Range.closedOpen(204020, 228420), 37);
        levelRangeMap.put(Range.closedOpen(228420, 254740), 38);
        levelRangeMap.put(Range.closedOpen(254740, 283040), 39);
        levelRangeMap.put(Range.closedOpen(283040, 313420), 40);
        levelRangeMap.put(Range.closedOpen(313420, 345940), 41);
        levelRangeMap.put(Range.closedOpen(345940, 380680), 42);
        levelRangeMap.put(Range.closedOpen(380680, 417720), 43);
        levelRangeMap.put(Range.closedOpen(417720, 457140), 44);
        levelRangeMap.put(Range.closedOpen(457140, 499000), 45);
        levelRangeMap.put(Range.closedOpen(499000, 543380), 46);
        levelRangeMap.put(Range.closedOpen(543380, 590360), 47);
        levelRangeMap.put(Range.closedOpen(590360, 640020), 48);
        levelRangeMap.put(Range.closedOpen(640020, 692440), 49);
        levelRangeMap.put(Range.closedOpen(692440, 747700), 50);
        levelRangeMap.put(Range.closedOpen(747700, 805860), 51);
        levelRangeMap.put(Range.closedOpen(805860, 867000), 52);
        levelRangeMap.put(Range.closedOpen(867000, 931200), 53);
        levelRangeMap.put(Range.closedOpen(931200, 998540), 54);
        levelRangeMap.put(Range.closedOpen(998540, 1069080), 55);
        levelRangeMap.put(Range.closedOpen(1069080, 1142920), 56);
        levelRangeMap.put(Range.closedOpen(1142920, 1220120), 57);
        levelRangeMap.put(Range.closedOpen(1220120, 1300760), 58);
        levelRangeMap.put(Range.closedOpen(1300760, 1384920), 59);
        levelRangeMap.put(Range.closedOpen(1384920, 1472660), 60);
        levelRangeMap.put(Range.closedOpen(1472660, 1564080), 61);
        levelRangeMap.put(Range.closedOpen(1564080, 1659240), 62);
        levelRangeMap.put(Range.closedOpen(1659240, 1758220), 63);
        levelRangeMap.put(Range.closedOpen(1758220, 1861100), 64);
        levelRangeMap.put(Range.closedOpen(1861100, 1967940), 65);
        levelRangeMap.put(Range.closedOpen(1967940, 2078840), 66);
        levelRangeMap.put(Range.closedOpen(2078840, 2193860), 67);
        levelRangeMap.put(Range.closedOpen(2193860, 2313080), 68);
        levelRangeMap.put(Range.closedOpen(2313080, 2436580), 69);
        levelRangeMap.put(Range.closedOpen(2436580, 2564420), 70);
        levelRangeMap.put(Range.closedOpen(2564420, 2696700), 71);
        levelRangeMap.put(Range.closedOpen(2696700, 2833480), 72);
        levelRangeMap.put(Range.closedOpen(2833480, 2974840), 73);
        levelRangeMap.put(Range.closedOpen(2974840, 3120860), 74);
        levelRangeMap.put(Range.closedOpen(3120860, 3271620), 75);
        levelRangeMap.put(Range.closedOpen(3271620, 3427180), 76);
        levelRangeMap.put(Range.closedOpen(3427180, 3587620), 77);
        levelRangeMap.put(Range.closedOpen(3587620, 3753040), 78);
        levelRangeMap.put(Range.closedOpen(3753040, 3923480), 79);
        levelRangeMap.put(Range.closedOpen(3923480, 4099040), 80);
        levelRangeMap.put(Range.closedOpen(4099040, 4279800), 81);
        levelRangeMap.put(Range.closedOpen(4279800, 4465820), 82);
        levelRangeMap.put(Range.closedOpen(4465820, 4657180), 83);
        levelRangeMap.put(Range.closedOpen(4657180, 4853960), 84);
        levelRangeMap.put(Range.closedOpen(4853960, 5056240), 85);
        levelRangeMap.put(Range.closedOpen(5056240, 5264080), 86);
        levelRangeMap.put(Range.closedOpen(5264080, 5477580), 87);
        levelRangeMap.put(Range.closedOpen(5477580, 5696800), 88);
        levelRangeMap.put(Range.closedOpen(5696800, 5921820), 89);
        levelRangeMap.put(Range.closedOpen(5921820, 6152720), 90);
        levelRangeMap.put(Range.closedOpen(6152720, 6389560), 91);
        levelRangeMap.put(Range.closedOpen(6389560, 6632440), 92);
        levelRangeMap.put(Range.closedOpen(6632440, 6881420), 93);
        levelRangeMap.put(Range.closedOpen(6881420, 7136580), 94);
        levelRangeMap.put(Range.closedOpen(7136580, 7398000), 95);
        levelRangeMap.put(Range.closedOpen(7398000, 7665740), 96);
        levelRangeMap.put(Range.closedOpen(7665740, 7939900), 97);
        levelRangeMap.put(Range.closedOpen(7939900, 8220540), 98);
        levelRangeMap.put(Range.closedOpen(8220540, 8507740), 99);
        levelRangeMap.put(Range.closedOpen(8507740, 8801580), 100);
    }

    private static void printCode() {
        int[][] levelArr = {
                {480, 1},
                {480, 2},
                {480, 3},
                {480, 4},
                {480, 5},
                {480, 6},
                {480, 7},
                {480, 8},
                {480, 9},
                {480, 10},
                {980, 11},
                {980, 12},
                {980, 13},
                {980, 14},
                {980, 15},
                {980, 16},
                {980, 17},
                {980, 18},
                {980, 19},
                {980, 20},
                {1460, 21},
                {2920, 22},
                {4400, 23},
                {5860, 24},
                {7320, 25},
                {8780, 26},
                {10240, 27},
                {11720, 28},
                {12680, 29},
                {13660, 30},
                {14500, 31},
                {15960, 32},
                {17480, 33},
                {19100, 34},
                {20780, 35},
                {22560, 36},
                {24400, 37},
                {26320, 38},
                {28300, 39},
                {30380, 40},
                {32520, 41},
                {34740, 42},
                {37040, 43},
                {39420, 44},
                {41860, 45},
                {44380, 46},
                {46980, 47},
                {49660, 48},
                {52420, 49},
                {55260, 50},
                {58160, 51},
                {61140, 52},
                {64200, 53},
                {67340, 54},
                {70540, 55},
                {73840, 56},
                {77200, 57},
                {80640, 58},
                {84160, 59},
                {87740, 60},
                {91420, 61},
                {95160, 62},
                {98980, 63},
                {102880, 64},
                {106840, 65},
                {110900, 66},
                {115020, 67},
                {119220, 68},
                {123500, 69},
                {127840, 70},
                {132280, 71},
                {136780, 72},
                {141360, 73},
                {146020, 74},
                {150760, 75},
                {155560, 76},
                {160440, 77},
                {165420, 78},
                {170440, 79},
                {175560, 80},
                {180760, 81},
                {186020, 82},
                {191360, 83},
                {196780, 84},
                {202280, 85},
                {207840, 86},
                {213500, 87},
                {219220, 88},
                {225020, 89},
                {230900, 90},
                {236840, 91},
                {242880, 92},
                {248980, 93},
                {255160, 94},
                {261420, 95},
                {267740, 96},
                {274160, 97},
                {280640, 98},
                {287200, 99},
                {293840, 100}
        };
        int sum = 0;
        for (int i = 0; i < levelArr.length; i++) {
            String str = "levelRangeMap.put(Range.closedOpen(";
            str = str + sum + ",";
            sum = sum + levelArr[i][0];
            str = str + sum + ")," + levelArr[i][1] + ");";
            System.out.println(str);
        }
    }

}
