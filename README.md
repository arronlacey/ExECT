# ExECT

ExECT (Extraction of Epilepsy Clinical Text) uses the open source GATE framework to build an application to extracts epilepsy information from clinic letters. It makes use of the Bio-YODIE GATE plugin (https://gate.ac.uk/applications/bio-yodie.html) as well as the South London and Maudsley medication application (https://github.com/KHP-Informatics/brc-gate-pharmacotherapy)

## Instructions

1. Download GATE (>= version 8.4.1) https://gate.ac.uk/download/
2. Download and build the Bio-YODIE plugin https://github.com/GateNLP/Bio-YODIE
3. In the top level of the Bio-YODIE folder, clone/copy the resources folder from this repository
4. In GATE, using the option to restore application from file, run "swansea-main-bio.xgapp"
5. Add your own clinic letters and run the swansea-main-bio processing resource from within GATE on your letters.

## SLaM medication application

This pipeline uses JAPE files and gazetteers built as part of the SLaM medication application (copyright license included). Some of these resources have been modified, but some are used without modifications and as such share the same JAPE/gazetteer filenames as those found at https://github.com/KHP-Informatics/brc-gate-pharmacotherapy. You do not need to download these resources as they are included as part of the Epilepsy application.

## Application information

This application has been validated by an epilepsy clinician using a test set of 200 letters unseen to both the developer and the clinician. The following categories are extracted by the application:

* Clinic Date
* Date of Birth
* Epilepsy diagnosis confirmed
* Epilepsy type
* Seizure type
* Seizure frequency
* Prescriptions
* EEG scan outcomes (normal/abnormal)
* MRI scan outcomes (normal/abnormal)
* CT scan outcomes (normal/abnormal)

## Contact

Any technical questions please contact Arron Lacey (a.s.lacey@swansea.ac.uk) or submit an issue.

