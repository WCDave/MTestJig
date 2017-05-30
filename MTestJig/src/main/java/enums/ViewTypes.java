package enums;

import main.ExternalView;
import main.IExternalViewStrategy;
import main.LaunchPointStrategy;
import main.RocketViewStrategy;

public enum ViewTypes {

  SLEW(new IExternalViewStrategy<ExternalView>() {
    @Override
    public void applyToView(ExternalView view) {
    }

    @Override
    public void reset() {

    }
  }, "Fixed View"),

  ROCKET(new LaunchPointStrategy(), "Rocket View"),

  GEOSYNCH(new RocketViewStrategy(), "Geo-Synch View");

  private IExternalViewStrategy strategy;
  private String description;



  private ViewTypes(IExternalViewStrategy strategy, String desc) {
    this.strategy = strategy;
    this.description = desc;
  }


  public IExternalViewStrategy getViewStrategy() {
    return strategy;
  }

  public String getDescription() {
    return description;
  }



}
