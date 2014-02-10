molindo-mysql-collations-lib
============================

JNI bridge to MySQL's collation-based compare functions provided by libmysqlclient (MySQL's C client API).

See [Introducing: molindo-mysql-collations-lib](http://techblog.molindo.at/2014/02/introducing-molindo-mysql-collations-lib.html)
on [Molindo Techblog](http://techblog.molindo.at/) for details.

Usage:
------

    import at.molindo.mysqlcollations.lib.Collation;

    Collation comparator = Collation.get("latin1_general_ci");
    int val = comparator.compare("Foo", "foo");

Maven:
------

    <dependency>
      <groupId>at.molindo</groupId>
      <artifactId>molindo-mysql-collations-lib</artifactId>
      <version>0.1.0</version>
    </dependency>

