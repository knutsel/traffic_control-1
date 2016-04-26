package data;

import com.comcast.cdn.traffic_control.traffic_router.neustar.data.TarExtractor;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TarExtractor.class, File.class, TarArchiveInputStream.class})
public class TarExtractorTest {

	@Before
	public void before() {
		LogManager.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%d %-5p [%c]: %m%n")));
	}

	@Test
	public void itExtractsTarFile() throws Exception {
		TarArchiveInputStream tarArchiveInputStream = mock(TarArchiveInputStream.class);
		whenNew(TarArchiveInputStream.class).withArguments(any(InputStream.class)).thenReturn(tarArchiveInputStream);

		when(tarArchiveInputStream.getNextTarEntry()).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocationOnMock) {
				count++;
				if (count == 1) {
					TarArchiveEntry tarArchiveEntry = mock(TarArchiveEntry.class);
					when(tarArchiveEntry.getName()).thenReturn("data.gpdb");
					when(tarArchiveEntry.isFile()).thenReturn(true);
					return tarArchiveEntry;
				}

				if (count == 2) {
					TarArchiveEntry tarArchiveEntry = mock(TarArchiveEntry.class);
					when(tarArchiveEntry.getName()).thenReturn("IpV6Data");
					when(tarArchiveEntry.isDirectory()).thenReturn(true);
					return tarArchiveEntry;
				}

				return null;
			}
		});

		File directory = mock(File.class);

		File fileInTar = spy(mock(File.class));
		when(fileInTar.createNewFile()).thenReturn(true);
		whenNew(File.class).withArguments(directory, "data.gpdb").thenReturn(fileInTar);

		File directoryInTar = spy(mock(File.class));
		when(directoryInTar.createNewFile()).thenReturn(true);
		whenNew(File.class).withArguments(directory, "IpV6Data").thenReturn(directoryInTar);

		FileOutputStream fileOutputStream = mock(FileOutputStream.class);
		whenNew(FileOutputStream.class).withArguments(fileInTar).thenReturn(fileOutputStream);

		when(tarArchiveInputStream.read(any(byte[].class))).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocationOnMock) {
				count++;

				return (count == 1) ? new Integer(654321) : new Integer(-1);
			}
		});

		InputStream inputStream1 = mock(InputStream.class);
		TarExtractor tarExtractor = new TarExtractor();
		assertThat(tarExtractor.extractTo(directory, inputStream1), equalTo(directory));

		verify(fileInTar).createNewFile();
		verify(fileOutputStream).write(any(byte[].class), eq(0), eq(654321));
		verify(fileOutputStream).close();
		verifyNoMoreInteractions(fileOutputStream);

		verifyZeroInteractions(directoryInTar);
	}

	class SimpleFilenameFilter implements FileFilter {
		String name;

		@Override
		public boolean accept(File pathname) {
			return this.name.equals(pathname.getName());
		}
	}
}