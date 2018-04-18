package gov.dhs.nppd.csc.nsd.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class PipelineUtilsTest {
	@Test
	public void shouldGetListOfFiles() throws IOException {
		List<String> fileNames = PipelineUtils.getFileNamesFromFolder(new File("/home/kiet/code"));
		fileNames.stream().forEach(name -> System.out.println(name));
	}

}
