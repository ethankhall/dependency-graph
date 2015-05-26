package io.ehdev.dependency

import groovy.transform.TupleConstructor

@TupleConstructor
class DependencyNode {
    int id
    String name
    Set<DependencyNode> parents = new HashSet<>()
    Set<DependencyNode> children = new HashSet<>()

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DependencyNode that = (DependencyNode) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return name.hashCode()
    }
}
