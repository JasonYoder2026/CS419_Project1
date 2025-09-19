import java.util.*;

/**
 * TODO: implement the SRTF (Shortest Remaining Time First) scheduling algorithm.
 *
 * SRTF is also known as preemptive SJF
 */

public class SRTF extends Algorithm{

    private final LinkedList<Process> readyQueue = new LinkedList<>();

    private final Queue<Process> processesToArrive;

    private int now = 0;

    public SRTF(List<Process> allProcesses){
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses);
    }

    @Override
    public void schedule(){
        System.out.println("Shortest Remaining Time First:");

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

            //Have a loop that sorts through the ready queue, organizing by bursttime
            for (int i = 0; i < readyQueue.size(); i++) {
                int minIndex = i;
                for (int j = i + 1; j < readyQueue.size(); j++) {
                    if (readyQueue.get(j).getBurstTime() < readyQueue.get(minIndex).getBurstTime()) {
                        if (minIndex != i) {
                            Process tempProcess = readyQueue.get(i);
                            readyQueue.set(i, readyQueue.get(minIndex));
                            readyQueue.set(minIndex, tempProcess);
                        }

                    }
                }
            }


            //Compare the quickest item in readyqueue to the current & switch
            if(currentProcess.getBurstTime() <= readyQueue.get(0).getBurstTime()){
                Process tempProcess = currentProcess;
                currentProcess = readyQueue.get(0);
                readyQueue.set(0, tempProcess);
            }

            int runTime = currentProcess.getBurstTime();
            System.out.print("At time " + now + ": ");
            CPU.run(currentProcess, runTime);

            now += runTime;

            currentProcess.setRemainingTime(0);
            currentProcess.setFinishTime(now);

            if(currentProcess.getRemainingTime() == 0){
                currentProcess = readyQueue.getFirst();
                readyQueue.removeFirst();
            }

            // If any processes have arrived by 'now', add them to ready queue
            while(!processesToArrive.isEmpty() && processesToArrive.peek().getArrivalTime()<=now){
                readyQueue.add(processesToArrive.remove());
            }
        }
    }
}

