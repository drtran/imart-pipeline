package gov.dhs.nppd.csc.nsd.pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
	public static void main(String... args) throws IOException {
		if (args.length < 1) {
			showInstruction();
			System.exit(0);
		}

		String cmd = args[0];

		switch (cmd) {
		case "list-files":
			String folder = ".";

			if (args.length < 2) {
				System.out.println("missing folder name - use current folder instead");
			} else {
				folder = args[1];
			}

			listFiles(folder);
			break;

		case "list-files-contains-of":
		case "list-files-ends-with":
		case "list-files-begins-with":
			if (args.length < 3) {
				printErrorAndInstructionThenExit("missing filter");
			} else if (args.length < 2) {
				printErrorAndInstructionThenExit("missing folder");
			}

			switch (cmd) {
			case "list-files-contains-of":
				listFilesContainsOf(args[1], args[2]);
				break;
			case "list-files-ends-with":
				listFilesEndsWith(args[1], args[2]);
				break;
			case "list-files-begins-with":
				listFilesBeginsWith(args[1], args[2]);
				break;
			}
			
			break;
			
		case "make-run-all-ciap-tests":
			makeRunAllCiapTests();

			break;

		default:
			printErrorAndInstructionThenExit(String.format("%s: unsupported command.", cmd));
			return;
		}

		System.out.println("complete.");
		System.exit(0);

	}
	
	private static void printErrorAndInstructionThenExit(String errMsg) {
		System.out.println(errMsg);
		showInstruction();
		System.exit(-1);
	}

	private static String makeRunAllCiapTests() throws IOException {
		StringBuilder sb = new StringBuilder();

		String bashCmd = "#!/usr/bin/env bash";
		sb.append(bashCmd).append("\n");

		List<String> fileNames = listFilesContainsOf(".", "_test.rb");
		fileNames.stream().forEach(fileName -> sb.append("rake test ").append(fileName.substring(2)).append("\n"));

		String runAllCiapTestFileName = "./run-all-ciap-tests.sh";

		File runAllCiapTestFile = new File(runAllCiapTestFileName);

		if (runAllCiapTestFile.exists()) {
			runAllCiapTestFile.delete();
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(runAllCiapTestFile));
			bw.write(sb.toString());
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
		return sb.toString();
	}

	private static void showInstruction() {
		System.out.println("java gov.dhs.nppd.csc.nsd.pipeline.Main cmd parameters");
		System.out.println(" cmd:\n");

		System.out.println(" - list-files:\n");
		System.out.println("   parameters:");
		System.out.println("     - folder-name (optional - default: current folder): i.e. /tmp\n");
		System.out.println("   output: list of file names");

		System.out.println(" - list-files-contains-of | list-files-ends-with | list-files-begins-with:\n");
		System.out.println("   parameters:");
		System.out.println("     - folder-name (required): i.e. /tmp");
		System.out.println("     - filter (required): i.e. _test.rb");
		System.out.println("   output: list of file names\n");

		System.out.println(" - make-run-all-ciap-tests:\n");
		System.out.println("   parameters: none");
		System.out.println("   output: ./run-all-ciap-tests.sh");
	}

	private static List<String> listFilesContainsOf(String folder, String filter) throws IOException {
		List<String> fileNames = PipelineUtils.getFileNamesFromFolder(new File(folder));
		List<String> filteredFileNames = fileNames.stream().filter(fileName -> fileName.contains(filter))
				.collect(Collectors.toList());
		printList(filteredFileNames);
		return filteredFileNames;
	}

	private static List<String> listFilesBeginsWith(String folder, String filter) throws IOException {
		List<String> fileNames = PipelineUtils.getFileNamesFromFolder(new File(folder));
		List<String> filteredFileNames = fileNames.stream().filter(fileName -> fileName.startsWith(filter))
				.collect(Collectors.toList());
		printList(filteredFileNames);
		return filteredFileNames;

	}

	private static void printList(List<String> filteredFileNames) {
		filteredFileNames.stream().forEach(filteredFileName -> System.out.println(filteredFileName));
		System.out.println(String.format("%d files found", filteredFileNames.size()));
	}

	private static List<String> listFilesEndsWith(String folder, String filter) throws IOException {
		List<String> fileNames = PipelineUtils.getFileNamesFromFolder(new File(folder));
		List<String> filteredFileNames = fileNames.stream().filter(fileName -> fileName.endsWith(filter))
				.collect(Collectors.toList());
		printList(filteredFileNames);
		return filteredFileNames;

	}

	private static List<String> listFiles(String folder) throws IOException {
		List<String> fileNames = PipelineUtils.getFileNamesFromFolder(new File(folder));
		printList(fileNames);
		return fileNames;
	}
}
