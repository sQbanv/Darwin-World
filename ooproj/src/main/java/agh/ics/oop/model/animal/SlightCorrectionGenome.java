package agh.ics.oop.model.animal;

public class SlightCorrectionGenome extends AbstractGenotype {
    public SlightCorrectionGenome(int n){
        super(n);
    }

    public SlightCorrectionGenome(int n, int minMutations, int maxMutations, Animal parentA, Animal parentB){
        super(n, minMutations, maxMutations, parentA, parentB);
    }
    
    @Override
    public void mutate(){
        int position = random.nextInt(this.getGenes().size());
        int upOrDown = random.nextBoolean() ? 1 : -1;

        if(this.getGenes().get(position) == 0 && upOrDown == -1){
            this.getGenes().set(position,7);
        } else if (this.getGenes().get(position) == 7 && upOrDown == 1){
            this.getGenes().set(position,0);
        } else {
            this.getGenes().set(position,this.getGenes().get(position) + upOrDown);
        }
    }
}
