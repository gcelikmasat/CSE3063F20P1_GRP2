from Metric import *
from ReadFile import *
import os
from WriteOutput import WriteOutput

# Poll System Class is the class that holds the main method
class PollSystem():
    def main(self):
        # Initialize Log File Format
        logging.basicConfig(handlers=[logging.FileHandler('LogFile.log', 'w', 'utf-8')], level=logging.INFO,
                            format='%(asctime)s - %(levelname)s - %(message)s')
        logging.info('Started')

        # Create Read File
        read = ReadFile()
        # Empty lists and paths to read files in working directory
        Polls = []
        BYSLIST = ""
        tempList = os.listdir(os.getcwd())

        # For each file if that file ends with csv and if it is a answer key then call readAnswerKey
        for files in tempList:
            if files.endswith(".csv"):
                if "AnswerKey" in files:
                    read.readAnswerKey(files)
                else:
                    # Else add it to Polls
                    Polls.append(files)
                    # If file ends with xls then it is the student list
            elif files.endswith(".xls"):
                BYSLIST = files

        # read student list by calling readStudentFile function
        students = read.readStudentFile(BYSLIST)

        # Initialize which poll is being used
        usedPolls = []
        for report in Polls:
            # Read report files and add them to used polls
            read.readReportFile(report, usedPolls)

        # For each student match their answers with correct answers
        for student in students:
            student.setCorrectAnswer()
            student.toString()
            
        # Create write Output object
        write = WriteOutput(students, usedPolls)
        # Write output of each students result for each Poll
        write.writeGlobalResult()

        # Create Metric
        metric = Metric(read)
        # Write results to the Global File
        metric.calculateMetrics(usedPolls)

        # Print results of each question and print pie charts and histograms to excel file
        metric.calculateQuestionDistrubition(usedPolls)

        # Print attendance
        write.writeAttandence(metric)


if __name__ == '__main__':
    PollSystem().main()
