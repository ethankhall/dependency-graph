package io.ehdev.dependency

import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.atomic.AtomicInteger

class GraphTask extends DefaultTask {

    Map<String, DependencyNode> dependencyNodeMap = [:]
    AtomicInteger nodeId = new AtomicInteger(0)

    @OutputFile
    File nodeJson = new File(project.buildDir, 'node.json')

    @OutputFile
    File edgeJson = new File(project.buildDir, 'edge.json')

    @TaskAction
    def createGraph() {
        def root = nodeCreator(project.name)
        dependencyNodeMap[""] = root
        project.configurations.compile.resolvedConfiguration.firstLevelModuleDependencies.each {
            addNode(root, it)
        }

        createJsNodes()
    }

    def createJsNodes() {
        def nodes = []
        dependencyNodeMap.collectEntries { key, value ->
            nodes << [ id: value.id, label: value.name ]
        }

        def edges = []
        dependencyNodeMap.each { key, value ->
            value.children.each { child ->
                edges <<  [from: value.id, to: child.id]
            }
        }

        nodeJson.text = new JsonBuilder(nodes).toPrettyString()
        edgeJson.text = new JsonBuilder(edges).toPrettyString()
    }

    def addNode(DependencyNode root, ResolvedDependency dep) {
        def qualifiedName = "$dep.moduleGroup:$dep.moduleName" as String
        if(dependencyNodeMap[qualifiedName]){
            dependencyNodeMap[qualifiedName].parents << root
        } else {
            def child = nodeCreator(qualifiedName)

            root.children << child
            dependencyNodeMap[qualifiedName] = child

            dep.children.each { addNode(child, it)}
        }
    }

    def nodeCreator(String name) {
        return new DependencyNode(id: nodeId.getAndIncrement(), name: name)
    }
}
