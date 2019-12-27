package com.moowork.gradle.node.task;

import com.moowork.gradle.node.NodePlugin;
import com.moowork.gradle.node.exec.NodeExecRunner;
import groovy.lang.Closure;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.gradle.api.tasks.PathSensitivity.RELATIVE;


public class NodeTask extends DefaultTask {

	protected NodeExecRunner runner;
	private File script;
	private List<String> args = new ArrayList<>();
	private List<String> options = new ArrayList<>();
	private ExecResult result;

	public NodeTask() {
		this.setGroup(NodePlugin.NODE_GROUP);
		this.runner = new NodeExecRunner(this.getProject());
		dependsOn(SetupTask.NAME);
	}

	public void setScript(final File value) {
		this.script = value;
	}

	public void setArgs(final List<String> value) {
		this.args = value;
	}

	public void addArgs(final String... args) {
		this.args.addAll(Arrays.asList(args));
	}

	public void setOptions(final List<String> value) {
		this.options = value;
	}

	public void setEnvironment(final Map<String, String> value) {
		this.runner.getEnvironment().putAll(value);
	}

	public void setWorkingDir(final File workingDir) {
		this.runner.setWorkingDir(workingDir);
	}

	public void setIgnoreExitValue(final boolean value) {
		this.runner.setIgnoreExitValue(value);
	}

	public void setExecOverrides(final Closure<ExecSpec> closure) {
		this.runner.setExecOverrides(closure);
	}

	@Internal
	public ExecResult getResult() {
		return this.result;
	}

	@InputFile
	@PathSensitive(RELATIVE)
	public File getScript() {
		if (this.script != null && this.script.isDirectory()) {
			getLogger().warn("Using the NodeTask with a script directory ({}) is deprecated. " + "It will no longer be supported in the next major version.", this.script);
			return new File(this.script, "index.js");
		}

		return this.script;
	}

	@Input
	public List<String> getArgs() {
		return this.args;
	}

	@Input
	public List<String> getOptions() {
		return this.options;
	}

	@Nested
	public NodeExecRunner getRunner() {
		return this.runner;
	}

	@TaskAction
	public void exec() {
		if (this.script == null) {
			throw new IllegalStateException("Required script property is not set.");
		}

		List<String> execArgs = new ArrayList<>(this.options);
		execArgs.add(this.script.getAbsolutePath());
		execArgs.addAll(this.args);

		this.runner.setArguments(execArgs);
		this.result = this.runner.execute();
	}
}
