import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnterScore } from './enter-score';

describe('EnterScore', () => {
  let component: EnterScore;
  let fixture: ComponentFixture<EnterScore>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnterScore],
    }).compileComponents();

    fixture = TestBed.createComponent(EnterScore);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
