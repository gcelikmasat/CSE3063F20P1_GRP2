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
        tempListAnswer=os.listdir(os.getcwd()+"/Answer")
        tempListQuiz=os.listdir(os.getcwd()+"/Quiz")


        # define the name of the directory to be created

        path = os.getcwd() + "/Result"
        try:
            os.mkdir(path)
        except OSError:
            print("Creation of the directory %s failed" % path)
        path2 = os.getcwd() + "/Result/Students"
        try:
            os.mkdir(path2)
        except OSError:
            print("Creation of the directory %s failed" % path2)



        for files in tempListQuiz:
            Polls.append(files)

        for files in tempListAnswer:
            read.readAnswerKey(files)

        # For each file if that file ends with csv and if it is a answer key then call readAnswerKey
        for files in tempList:
            # If file ends with xls then it is the student list
            if files.endswith(".xls"):
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
        print("Calculating metrics. Please wait!!!")
        write.writeGlobalResult()
        write.writeStudentResult()

        # Create Metric
        metric = Metric(read)
        # Write results to the Global File
        metric.calculateMetrics(usedPolls)

        # Print attendance
        write.writeAttandence(metric)

        # Print results of each question and print pie charts and histograms to excel file
        metric.calculateQuestionDistrubition(usedPolls)


        

if __name__ == '__main__':
    PollSystem().main()
