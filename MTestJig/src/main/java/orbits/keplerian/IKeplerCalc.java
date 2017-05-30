package orbits.keplerian;

import java.util.concurrent.Future;

/**
 * Created by dbutterw on 5/9/2017.
 */
public interface IKeplerCalc {

  Future<IOrbit> getOrbit();
}
