package autopilot.pathPrecision.impl;

import autopilot.pathPrecision.ICorridorSegment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarthGravityTurnCorridorFactory extends AbstractCorridorFactory<Integer, Integer> {

  private static Map<Integer, Integer> timePitchMap;
  private static Map<Integer, Integer> timeAltMap;
  private static Map<Integer, Integer> pitchAltMap;

  static {
    timePitchMap = new HashMap<Integer, Integer>() {{
      put(10, 68);
      put(20, 64);
      put(30, 57);
      put(40, 51);
      put(50, 42);
      put(60, 36);
      put(70, 30);
      put(80, 25);
      put(90, 21);
      put(100, 18);
      put(110, 15);
      put(120, 12);
      put(130, 10);
      put(140, 9);
      put(150, 9);
      put(160, 8);
      put(170, 6);
      put(180, 5);
      put(190, 4);
      put(200, 3);
      put(210, 3);
      put(220, 2);
      put(230, 1);
      put(240, 0);
      put(250, 0);
    }};

    timeAltMap = new HashMap<Integer,Integer>() {{
      put(0,6371028);
      put(10,6373349);
      put(20,6376334);
      put(30,6379737);
      put(40,6383482);
      put(50,6387590);
      put(60,6392072);
      put(70,6396901);
      put(80,6402030);
      put(90,6407384);
      put(100,6412865);
      put(110,6418371);
      put(120,6423738);
      put(130,6429106);
      put(140,6434182);
      put(150,6438981);
      put(160,6443456);
      put(170,6447381);
      put(180,6451307);
      put(190,6454648);
      put(200,6457589);
      put(210,6460140);
      put(220,6462314);
      put(230,6464134);
      put(240,6465631);
      put(250,6466845);
      put(260,6467820);
      put(270,6468610);
      put(280,6469275);
      put(290,6469884);
      put(300,6470500);
      put(310,6471132);
    }};

    pitchAltMap = new HashMap<Integer, Integer>() {{
      put(1,6502022);
      put(2,6498969);
      put(3,6492943);
      put(4,6471675);
      put(5,6469169);
      put(6,6461841);
      put(7,6457860);
      put(8,6452844);
      put(9,6450546);
      put(10,6435801);
      put(11,6431131);
      put(12,6430214);
      put(13,6424256);
      put(14,6423233);
      put(15,6422185);
      put(16,6421122);
      put(18,6420058);
      put(19,6414001);
      put(20,6413963);
      put(22,6410953);
      put(23,6405985);
      put(25,6404070);
      put(27,6403224);
      put(29,6400448);
      put(31,6396767);
      put(32,6395473);
      put(33,6394180);
      put(36,6392693);
      put(39,6390311);
      put(42,6387036);
      put(45,6384868);
      put(48,6383807);
      put(51,6381847);
      put(55,6379983);
      put(59,6378209);
      put(62,6376529);
      put(66,6374954);
      put(68,6373504);
    }
    };
  }


  private static int tolerance = 2;
  private static int altBias = -3000;

  public EarthGravityTurnCorridorFactory(Integer tolerance) {
    this.tolerance = tolerance;
    fillMap(pitchAltMap);
  }

  private void fillMap(Map<Integer, Integer> pitchAltMap) {
    List<Integer> sortingList = new ArrayList<Integer>(pitchAltMap.keySet());
    Collections.sort(sortingList);
    Integer lastFoundKey = sortingList.get(0);
    int dispCounter = 0;
    for(Integer i=1;i<90;i++) {
      if(pitchAltMap.get(i) == null) {
        dispCounter++;
      }
      else {
        if(dispCounter != 0) {
          int segSize = (-pitchAltMap.get(i) + pitchAltMap.get(lastFoundKey))/(dispCounter+1);
          for(int j = 0;j<dispCounter;j++) {
            pitchAltMap.put(new Integer(lastFoundKey+1+j), pitchAltMap.get(lastFoundKey)+(j+1)*segSize);
          }
        }
        lastFoundKey = i;
        dispCounter = 0;
      }
    }
  }

  @Override
  public ICorridorSegment<Integer, Integer> getCorridor(final Integer input) {
    //final int keyInput = input;
    ICorridorSegment<Integer, Integer> corridor = new GravityTurnPitchOnlyCorridorSegment(pitchAltMap.get(input) != null ? pitchAltMap.get(input) + altBias + tolerance : 0,
            pitchAltMap.get(input) != null ? pitchAltMap.get(input) + altBias - tolerance : 0,
            pitchAltMap.get(input) != null ? pitchAltMap.get(input) + altBias: 0);

    return corridor;
  }


}
