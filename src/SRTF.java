import java.util.*;

/**
 * TODO: implement the SRTF (Shortest Remaining Time First) scheduling algorithm.
 * SRTF is also known as preemptive SJF
 */
public class SRTF extends Algorithm{

    private final LinkedList<Process> readyQueue = new LinkedList<>();
    private final Queue<Process> processesToArrive;
    private final List<Process> allProcesses;
    private int now = 0;

    public SRTF(List<Process> allProcesses) {
        super(allProcesses);
        this.allProcesses = allProcesses;
        processesToArrive = new LinkedList<>(allProcesses);

        // Initialize remaining time for each process
        for (Process p : allProcesses) {
            p.setRemainingTime(p.getBurstTime());
        }
    }

    @Override
    public void schedule() {
        System.out.println("Shortest Remaining Time First:");

        Process currentProcess = null;

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty() || currentProcess != null) {

            while (!processesToArrive.isEmpty() && processesToArrive.peek().getArrivalTime() <= now) {
                readyQueue.add(processesToArrive.remove());
            }

            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            if (!readyQueue.isEmpty()) {
                if (currentProcess == null || currentProcess.getRemainingTime() > readyQueue.getFirst().getRemainingTime()) {
                    currentProcess = readyQueue.getFirst();
                }
            }

            if (currentProcess == null) {
                if (!processesToArrive.isEmpty()) {
                    now = processesToArrive.peek().getArrivalTime();
                    continue;
                } else {
                    break;
                }
            }

            System.out.println("At time " + now + ": CPU runs " + currentProcess + " for duration 1");
            CPU.run(currentProcess, 1);
            now += 1;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setFinishTime(now);
                readyQueue.remove(currentProcess);
                currentProcess = null;
            }
        }

        int totalWait = 0;
        for (Process p : allProcesses) {
            int waitingTime = p.getFinishTime() - p.getArrivalTime() - p.getBurstTime();
            System.out.println(p.getName() + "'s waiting time is " + waitingTime);
            totalWait += waitingTime;
        }

        double avgWait = (double) totalWait / allProcesses.size();
        System.out.println("Average wait time is " + avgWait);
    }
}