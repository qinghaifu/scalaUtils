Scala GetOpts
==========

simple scala library of parse command line options

# Usage
	<code>
		val opt = new GetOpts("times=i=m", "words=s");
    	opt.parse("-times 3 -words hi,world!");
	</code>
define the patterns as name=type=[m], name is the name of option, type is the option type, current support i as Integer, s as String, b as Boolean.	

details refer to HelloWorld.scala

# to build eclipse
1. sbt 
2. eclipse
3. import into eclipse
