package orbits;


import autopilot.AFCSTargetingStrategy;

import java.util.regex.Matcher;

public interface ICommandHandler<Pattern, T extends AFCSTargetingStrategy> {
  T handle(Matcher m);
}
