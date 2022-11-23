# Java

## JVM Monitoring Tips

VM limits (for Java services)
- JMX HeapMemoryAfterGCUse - memory usage within the JVM allocation. Using the AFTER garbage collection metric is important to get an accurate reading. It is important to monitor heap usage because Java services that run out of heap space spend a lot of time garbage collecting. This causes increases in service latency or makes the service stop entirely.
- JMX FileDescriptorUse - includes network sockets because the socket implementation uses a file descriptor. If the service runs out of file descriptors, it cannot write additional log files, and you may lose logs and metrics for the duration of the event. The service will also be unable to open any additional sockets, which will manifest as connection failures visible to customers.
- Snitch PercentSpaceInUse - Logging and metrics implementations write to disk, so you will lose logs and metrics if the service runs out of disk space. Unless you have tried it and caught all of the exceptions, running out of disk space will likely also cause IO or Monitoring exceptions to bubble up as service errors, which customers may see.
- Snitch CPU Utilization - Many services have CPU as the limiting factor in their scaling. High CPU utilization will eventually result in additional service latency, as the operating system will not be able to schedule all processes to run immediately. High CPU utilization can be mitigated by reducing load or adding hardware.

## Java Log Concept

### Loggers

Loggers are responsible for capturing events (called LogRecords)
and passing them to the appropriate Appender.

Loggers are objects that trigger log events.
Loggers are created and called in the code of your Java application, where they generate events
before passing them to an Appender. A class can have multiple independent Loggers
responding to different events, and you can nest Loggers under other Loggers to create a hierarchy.

Loggers provide several methods for triggering log events. However, before you can log an event, you need
to assign a level. Log levels determine the severity of the log and can be used to filter the event or send
it to a different Appender(for more information on log levels, see the Log Levels section).
The Logger.log() method requires a level in addition to a message.

logger.log(Level.WARNING, “This is a warning!”);

Most logging frameworks provide shorthand methods for logging at a particular level. For example, the following statement produces the same output as the previous statement.

logger.warning(“This is a warning!”);

### Appenders

Appenders (also called Handlers in some logging frameworks) are responsible for recording
log events to a destination. Appenders use Layouts to format events before sending them to an output.

Appenders forward logs from Loggers to an output destination.
During this process, log messages are formatted using a Layout before being
delivered to their final destination. Multiple Appenders can be combined to write log events to multiple
destinations. For instance, a single event can be simultaneously displayed in a console and written to a file.

Note that java.util.logging refers to Appenders as Handlers.

### Layouts

Layouts (also called Formatters in some logging frameworks)
are responsible for converting and formatting the data in a log event.
Layouts determine how the data looks when it appears in a log entry.

Layouts convert the contents of a log entry from one data type into another. Logging frameworks
provide Layouts for plain text, HTML, syslog, XML, JSON, serialized, and other logs.
Note that java.util.logging refers to Layouts as Formatters.
For example, java.util.logging provides two Layouts: the SimpleFormatter and the XMLFormatter.
SimpleFormatter, the default Layout for ConsoleHandlers, outputs plain text log entries

When your application makes a logging call, the Logger records the event in a LogRecord and forwards it
to the appropriate Appender. The Appender then formats the record using a Layout before
sending it a destination such as the console, a file, or another application.
Additionally, you can use one or more Filters to specify which Appenders should be used for which events.
Filters aren’t required, but they give you greater control over the flow of your log messages.

`APPLICATION >> LOGGER (FILTER OPTIONALLY) >> HANDLER (FILTER OPTIONALLY) >> OUTSIDE WORLD`

### Log Levels

SEVERE(HIGHEST LEVEL)
WARNING
INFO
CONFIG
FINE
FINER
FINEST(LOWEST LEVEL)

### More Info

<https://www.loggly.com/ultimate-guide/java-logging-basics>
