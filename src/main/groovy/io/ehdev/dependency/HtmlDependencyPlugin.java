package io.ehdev.dependency;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class HtmlDependencyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("generateDependendyJson", GraphTask.class);
    }
}
