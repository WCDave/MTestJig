package orbits;


import autopilot.AFCSTargetingStrategy;

import java.util.List;
import java.util.regex.Matcher;

public interface ICommandHandler<Matcher, T extends AFCSTargetingStrategy> {
  List<T> handle(Matcher m);
}
