package io.ehdev.dependency

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class GraphTaskTest extends Specification {

    def 'get simple dependency'() {
        setup:
        def project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'java'
        project.repositories.mavenCentral()

        project.dependencies.add('compile', 'commons-io:commons-io:2.4')

        GraphTask task = project.tasks.create('graphTask', GraphTask)
        task.createGraph()

        then:
        task.dependencyNodeMap.size() == 2
    }

    def 'get spring dependency'() {
        setup:
        def project = ProjectBuilder.builder().build()

        when:
        project.apply plugin: 'java'
        project.repositories.mavenCentral()

        project.dependencies.add('compile', 'org.springframework:spring-core:4.1.6.RELEASE')

        GraphTask task = project.tasks.create('graphTask', GraphTask)
        task.createGraph()

        then:
        task.dependencyNodeMap.size() == 3
    }
}
