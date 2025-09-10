
import java.util.*;

/**
 * TODO: Implement the non-preemptive SJF (Shortest-Job First) scheduling algorithm.
 */

public class SJF extends Algorithm {
    private final LinkedList<Process> readyQueue = new LinkedList<>();

    // Processes that have not yet arrived
    private final Queue<Process> processesToArrive;

    // The simulation clock
    private int now = 0;

    public SJF(List<Process> allProcessList){
        super(allProcessList);
        processesToArrive = new LinkedList<>(allProcessList);
    }

    @Override
    public void schedule() {
        System.out.println("Shortest Job First:");

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()) {
            if (readyQueue.isEmpty()) {
                Process process = processesToArrive.remove();
                if (now < process.getArrivalTime()) {
                    //advance the simulation clock to the next process's arrival time
                    now = process.getArrivalTime();
                }
                readyQueue.add(process);
            }


            Process currentProcess = readyQueue.getFirst();

            for (int i = 0; i < readyQueue.size(); i++) {
                if (currentProcess.getBurstTime() <= readyQueue.get(i).getBurstTime()) {
                    currentProcess = readyQueue.get(i);
                }
            }
            readyQueue.remove(currentProcess);


            int runTime = currentProcess.getBurstTime();
            System.out.print("At time " + now + ": ");
            CPU.run(currentProcess, runTime);

            now += runTime;

            currentProcess.setRemainingTime(0);
            currentProcess.setFinishTime(now);

            // If any processes have arrived by 'now', add them to ready queue
            while(!processesToArrive.isEmpty() && processesToArrive.peek().getArrivalTime()<=now){
                readyQueue.add(processesToArrive.remove());
            }
        }
    }
}
