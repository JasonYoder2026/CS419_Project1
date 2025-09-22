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
        int runStart = -1;

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty() || currentProcess != null) {

            while (!processesToArrive.isEmpty() && processesToArrive.peek().getArrivalTime() <= now) {
                readyQueue.add(processesToArrive.remove());
            }

            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            if (!readyQueue.isEmpty()) {
                if (currentProcess == null || currentProcess.getRemainingTime() > readyQueue.getFirst().getRemainingTime()) {
                    if (currentProcess != null) {
                        int duration = now - runStart;
                        System.out.println("At time " + runStart + ": CPU ran " + currentProcess + " for duration " + duration);
                    }
                    currentProcess = readyQueue.getFirst();
                    runStart = now;
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

            CPU.run(currentProcess, 1);
            now++;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

            if (currentProcess.getRemainingTime() == 0) {
                int duration = now - runStart;
                System.out.println("At time " + runStart + ": CPU ran " + currentProcess + " for duration " + duration);
                currentProcess.setFinishTime(now);
                readyQueue.remove(currentProcess);
                currentProcess = null;
            }
        }


    }
}