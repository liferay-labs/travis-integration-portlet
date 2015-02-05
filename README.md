# Travis Integration Portlet

## Motivation

More and more often, development teams adopt Continuous Integration as a good practice within development processes, and team members need to watch the results of its current work.

Travis-ci is a continuous integration service perfect for open-source projects, so many and many teams are using it as their preferred CI server.

As Travis provides many metrics by itself, I've decided to start a [Liferay](http://www.liferay.com) + [AlloyUI](http://www.alloyui.com) + [Travis](http://www.travis-ci.org) integration portlet that allows to display build status in an easy and portal-deployable way. For that, a Liferay portal Admin, can deploy this portlet in his portal, allowing its users to watch last builds statuses, in an user-friendly style, using AlloyUI built-in capabilities.

Below there are some screenshots of the current portlet, contributed by my UX friend [@jhidalgoreina](https://twitter.com/jhidalgoreina), thank you very much!:

<img title="Travis Integration Portlet Configuration" src="https://github.com/mdelapenya/travis-integration-portlet/raw/master/images/jenkins-integration-portlet-002.png" />
<img title="Travis Integration Portlet Sucessfull Build" src="https://github.com/mdelapenya/travis-integration-portlet/raw/master/images/jenkins-integration-portlet-003.png" />
<img title="Travis Integration Portlet Broken Build" src="https://github.com/mdelapenya/travis-integration-portlet/raw/master/images/jenkins-integration-portlet-005.png" />

## Configuration

There is a properties files under 'docroot/WEB-INF/src' named *portlet.properties*. This file contains some properties with values for connecting to the Travis-ci service. You can extend that file with the *-ext* name convention, creating a *portlet-ext.properties*, where you will put custom values for your application.

## v.1.0.0
* Apply Bootstrap fluid styles
* Supports saving a very basic way to define aliases for builds, using this pattern: TRAVIS_USER|JOB_NAME|JOB_ALIAS. This way long named builds can be shorten by an alias.
* Supports a new different display mode: jobs stack, where you can define multiple jobs that will be displayed in a stack, ordered by last build status
* Supports processing job names by a custom processor: i.e. removing dashes from job names
* Supports one display mode: default, as a traffic light: red/green status depending of last build status
* Job name is configurable
* Maximum number of builds to display is configurable

## License

This library, "Travis Integration Portlet", is free software ("Licensed Software"); you can redistribute it and/or modify it under the terms of the [GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl-2.1.html) as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; including but not limited to, the implied warranty of MERCHANTABILITY, NONINFRINGEMENT, or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the [GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl-2.1.html) along with this library; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
