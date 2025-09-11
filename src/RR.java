import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * TODO: implement the RR (Round Robin) scheduling algorithm
 */
public class RR extends Algorithm{

    private final Queue<Process> readyQueue = new LinkedList<>();

    private final Queue<Process> processesToArrive;

    private int now = 0;

    public RR(List<Process> allProcesses){
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses);
    }

    @Override
    public void schedule(){
        System.out.println("Round Robin:");

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()) {
            if (readyQueue.isEmpty()) {
                Process process = processesToArrive.remove();
                if (now < process.getArrivalTime()) {
                    now = process.getArrivalTime();
                }
                readyQueue.add(process);
            }
            int timeQuantum;
            if (readyQueue.peek().getName().substring(0, 1).equals("P")) {
                timeQuantum = 5;
            } else {
                timeQuantum = 10;
            }

            Process currentProcess = readyQueue.remove();

            System.out.print("At time " + now + ": ");
            if (currentProcess.getRemainingTime() > timeQuantum) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeQuantum);
                CPU.run(currentProcess, timeQuantum);
                now += timeQuantum;
                readyQueue.add(currentProcess);
            } else {
                CPU.run(currentProcess, currentProcess.getRemainingTime());
                currentProcess.setRemainingTime(0);
                now += timeQuantum;
                currentProcess.setFinishTime(now);
            }
            while(!processesToArrive.isEmpty() &&
                    processesToArrive.peek().getArrivalTime()<=now){
                readyQueue.add(processesToArrive.remove());
            }
        }
    }
}
