package fatalvirus;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTempMapper 
	extends Mapper<LongWritable, Text, Text, IntWritable> {

	private static final int MISSING = 9999;
	
	@Override
	protected void map(
			LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		// 입력파일에서 한줄씩 읽어서 년도를 추출
		String line = value.toString();
		String year = line.substring(15, 19);
		
		// 온도 추출시 기호가 들어있으면 그 다음 문자를 읽음
		int airTemp;
		if (line.charAt(87) == '+') {
			airTemp = Integer.parseInt(
					line.substring(88, 92));
		} else {
			airTemp = Integer.parseInt(
					line.substring(87, 92));
		}
		
		// 정상범위내 온도로 인식되면
		// 맵리듀스 작업대상으로 기록
		String quality = line.substring(92, 93);
		if (airTemp != MISSING && 
				quality.matches("[01459]")) {
			context.write(new Text(year), 
				new IntWritable(airTemp));
		}
		
	}

}
