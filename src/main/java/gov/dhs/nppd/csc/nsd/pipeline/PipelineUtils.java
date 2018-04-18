package gov.dhs.nppd.csc.nsd.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PipelineUtils {

	public static List<String> getFileNamesFromFolder(File folder) throws IOException {
		List<String> fileNames = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				// @formatter: off
				fileNames = Stream.of(fileNames, getFileNamesFromFolder(file))
								  .flatMap(Collection::stream)
						          .collect(Collectors.toList());
				// @formatter: on
			} else {
				fileNames.add(file.getPath());
			}
		}
		return fileNames;

	}

}
