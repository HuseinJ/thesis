FILENAME = presentation
BUILDDIR = build

all: $(BUILDDIR)/$(FILENAME).pdf

$(BUILDDIR)/$(FILENAME).pdf: $(FILENAME).tex
	mkdir -p $(BUILDDIR)
	pdflatex -interaction=nonstopmode -output-directory=$(BUILDDIR) $(FILENAME).tex
	pdflatex -interaction=nonstopmode -output-directory=$(BUILDDIR) $(FILENAME).tex

clean:
	rm -rf $(BUILDDIR)

.PHONY: all clean