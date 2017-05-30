package main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CommandBuilder implements ICommandBuilder {

  private Object executionObject;
  private String executionMethod;
  private Object cancelObject;
  private String cancelMethod;
  private Object[] eArgs, cArgs;

  Command createCommand(Object eo, String em, Object[] eArgs, Object co, String cm, Object[] cArgs) {
    try {
      Class clazzEo = eo.getClass();

      Class eArgClazz = eArgs != null ? eArgs[0].getClass() : null;
      final Method eMethod = eArgClazz != null ? clazzEo.getMethod(em, eArgClazz) : clazzEo.getMethod(em);

      Class cArgClazz = cArgs != null ? cArgs[0].getClass() : null;
      final Method cMethod = co!=null ? co.getClass().getMethod(cm, cArgClazz):null;

      Command result = new Command() {
        public void cancel() {
          try {
            if(cArgs != null) {
              if(cMethod != null)
                cMethod.invoke(co, cArgs);
            }
            else {
              if(cMethod != null)
                cMethod.invoke(co);
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        }

        public void execute() {
          try {
            if(eArgs != null) {
              eMethod.invoke(eo, eArgs);
            }
            else {
              eMethod.invoke(eo);
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        }
      };
      return result;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  public static CommandBuilder createCommand() {
    return new CommandBuilder();
  }

  @Override
  public Command buildCommand() {
    return createCommand(executionObject, executionMethod, eArgs, cancelObject, cancelMethod, cArgs);
  }

  public CommandBuilder setExecutionObject(Object executionObject) {
    this.executionObject = executionObject;
    return this;
  }

  public CommandBuilder setExecutionMethod(String executionMethod) {
    this.executionMethod = executionMethod;
    return this;
  }

  public CommandBuilder setCancelObject(Object cancelObject) {
    this.cancelObject = cancelObject;
    return this;
  }

  public CommandBuilder setCancelMethod(String cancelMethod) {
    this.cancelMethod = cancelMethod;
    return this;
  }

  public CommandBuilder seteArgs(Object[] eArgs) {
    this.eArgs = eArgs;
    return this;
  }

  public CommandBuilder setcArgs(Object[] cArgs) {
    this.cArgs = cArgs;
    return this;
  }

}
