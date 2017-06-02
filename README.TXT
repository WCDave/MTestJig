I use these VM Options
-Dlog4j.debug=true -Dlog4j.configuration=file:log4j.xml -Xmx4G -Xms2G -DdebugThreshold=DEBUG -Djava.util.Arrays.useLegacyMergeSort=true -Dsun.io.serialization.extendedDebugInfo=true


this one:
-Djava.util.Arrays.useLegacyMergeSort=true

Is either required or is related to performance...I forget, but better to use it than not.


The class with with the main method to run is in the MTestJig module:

main.MainApp